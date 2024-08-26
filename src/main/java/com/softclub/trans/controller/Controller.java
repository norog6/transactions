package com.softclub.trans.controller;

import com.softclub.trans.DTO.CustomerUpdateRequest;
import com.softclub.trans.DTO.OrderInvoice;
import com.softclub.trans.entity.*;
import com.softclub.trans.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@Swager
@RestController
public class Controller {
    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    RawDataService rawDataService;

    @Autowired
    SessionService sessionService;

    @Autowired
    CustomerService customerService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ClientService clientService;


    @Operation(
            summary = "Добавление нового пользователя: " +
                    "создать метод, который добавляет нового пользователя в таблицу Users с использованием " +
                    "TransactionTemplate. В случае ошибки транзакция должна быть откатана."
    )
    @PostMapping("/addUSer")
    public String AddUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return "Пользователь успешно добавлен.";
        } catch (Exception ex) {
            return "Ошибка при добавлении пользователя: " + ex.getMessage();
        }

    }

    @Operation(
            summary = "2. Обновление баланса счета: создать метод, " +
                    "который обновляет баланс счета пользователя в таблице Accounts." +
                    " Если баланс становится отрицательным, транзакция должна быть откатана.")
    @PostMapping("/balance")
    public ResponseEntity<String> updateBalance(@RequestBody Accounts accounts) {
        try {
            accountService.updateAccountBalance(accounts.getName(), accounts.getBalance());
            return ResponseEntity.ok("Balance updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(
            summary = "3. Создание заказа с генерацией новой транзакции: создать метод, который создает новый заказ " +
                    "в таблице Orders, а затем создает запись об оплате в таблице Payments, " +
                    "используя Propagation.REQUIRES_NEW. В случае ошибки при создании платежа," +
                    "только транзакция платежа должна быть откатана."
    )
    @PostMapping("/create")
    public String createOrderWithPayment(@RequestBody Order order) {
        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setOrderId(order.getId());
        orderService.createOrderWithPayment(order, payment);
        return "Order and payment processed";
    }

    @Operation(
            summary = "4. Добавление отзывов с вложенными транзакциями: создать метод, который добавляет отзыв в таблицу Reviews" +
                    " и обновляет рейтинг товара в таблице Products. " +
                    "Использовать вложенную транзакцию (Propagation.NESTED) для добавления отзыва." +
                    " Если добавление отзыва не удается, откатить только эту часть, сохранив остальные изменения.",
            description = "Предварительно надо создать продукт"
    )
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<String> addReview(@PathVariable Long productId, @RequestBody Review review) {

        try {
            productService.addReviewAndUpdateProduct(productId, review);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Product updated");
    }

    @Operation(
            summary = "5. Обновление профиля пользователя с требованием активной транзакции: " +
                    "создать метод, который обновляет профиль пользователя в таблице UserProfile. " +
                    "Метод должен использовать Propagation.MANDATORY, и если транзакция не активна," +
                    " должно быть выброшено исключение."
    )
    @PostMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(@RequestBody UserProfile userProfile) {
        try {
            userProfileService.Update(userProfile);
            return ResponseEntity.ok("User profile updated successfully.");
        } catch (IllegalTransactionStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Transaction is required but not active.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user profile: " + e.getMessage());
        }
    }

    @Operation(
            summary = "6. Просмотр заказов с условием существующей транзакции: создать метод," +
                    " который читает список заказов из таблицы Orders. Если транзакция существует, " +
                    "чтение должно быть частью транзакции, если нет – выполняется без транзакции. " +
                    "Использовать Propagation.SUPPORTS."
    )
    @GetMapping("/orders/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "7. Перевод денег между счетами с изоляцией READ_COMMITTED: " +
                    "создать метод для перевода денег между двумя счетами в таблице Accounts с использованием уровня изоляции" +
                    "READ_COMMITTED, чтобы предотвратить грязные чтения. Если на счету отправителя недостаточно средств," +
                    " транзакция должна быть откатана."
    )
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestParam String fromName,
                                                @RequestParam String toName,
                                                @RequestParam double amount) {
        try {
            accountService.transferMoney(fromName, toName, amount);
            return ResponseEntity.ok("Перевод выполнен успешно");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "8. Обновление инвентаря с изоляцией REPEATABLE_READ: создать метод, который обновляет" +
                    " количество товара в таблице Inventory при поступлении новой партии." +
                    " Использовать уровень изоляции REPEATABLE_READ, чтобы предотвратить неповторяющиеся чтения" +
                    " и убедиться, что данные не изменяются между двумя чтениями."
    )
    @PostMapping("/update")
    public String updateInventory(@RequestParam Long id, @RequestParam int newStock) {
        inventoryService.updateInventory(id, newStock);
        return "Inventory updated successfully!";
    }

    @Operation(
            summary = "9. Создание и удаление записей с изоляцией SERIALIZABLE: создать метод," +
                    " который создает новую запись о поставщике в таблице Suppliers, а затем удаляет старые записи, " +
                    "соответствующие определенному критерию. Использовать уровень изоляции SERIALIZABLE," +
                    " чтобы избежать фантомных чтений."
    )
    @PostMapping("/sup/create-clean")
    public String createAndCleanupSuppliers(@RequestBody Supplier supplier) {
        supplierService.createAndCleanupSuppliers(supplier);
        return "Supplier created and old records for this supplier cleaned up!";
    }

    @Operation(
            summary = "10. Чтение данных с изоляцией READ_UNCOMMITTED: " +
                    "создать метод, который читает необработанные данные из таблицы RawData с " +
                    "использованием уровня изоляции READ_UNCOMMITTED. Это может быть полезно для анализа данных," +
                    " где допустимы грязные чтения."
    )
    @GetMapping("/read-uncommitted")
    public List<RawData> readUncommittedData() {
        return rawDataService.readUncommittedData();
    }

    @Operation(
            summary = "11. Обновление заказа с блокировкой FOR UPDATE: создать метод," +
                    " который обновляет статус заказа в таблице Orders с использованием блокировки FOR UPDATE, " +
                    "чтобы предотвратить изменение статуса другими транзакциями до завершения текущей.",
            description = "я сделал обновление суммы"
    )
    @PutMapping("/{orderId}/update-order-amount")
    public String updateOrderSum(@PathVariable Long orderId, @RequestParam Double newAmount) {
        orderService.updateOrderSum(orderId, newAmount);
        return "Order status amount successfully.";
    }

    @Operation(
            summary = "12. Обновление профиля пользователя с оптимистичной блокировкой: создать метод, " +
                    "который обновляет профиль пользователя в таблице UserProfile с использованием" +
                    " оптимистичной блокировки (например, поля version). Если версия изменилась с момента последнего" +
                    " чтения, транзакция должна быть повторена."
    )
    @PutMapping("/{userProfileId}/updateUserProfile")
    public String updateUserProfile(@PathVariable Long userProfileId, @RequestBody UserProfile userProfile) {
        userProfileService.updateUserProfile(userProfileId, userProfile);
        return "User profile updated successfully";
    }

    @Operation(
            summary = "13. Удаление устаревших записей с пессимистичной блокировкой:" +
                    " создать метод, который удаляет устаревшие записи из таблицы Sessions," +
                    " используя пессимистичную блокировку для предотвращения параллельного удаления одних и тех же записей."
    )
    @DeleteMapping("/delete-sessions")
    public String deleteSessions() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);  // Устаревшие сессии старше 7 дней
        Date expirationTime = calendar.getTime();

        sessionService.removeExpiredSessions(expirationTime);
        return "Sessions delete succesfuly";
    }

    @Operation(
            summary = "14. Сохранение и обновление данных в одной транзакции с `@Transactional`" +
                    " и `TransactionTemplate`: создать метод, который сначала сохраняет данные в таблицу " +
                    "Customers с помощью аннотации @Transactional, а затем, используя TransactionTemplate," +
                    " обновляет связанные данные в таблице CustomerDetails. Если обновление не удается," +
                    " транзакция должна быть откатана."
    )
    @PostMapping("/customer")
    public ResponseEntity<String> createCustomerAndUpdateDetails(@RequestBody CustomerUpdateRequest customerUpdateRequest) {
        try {

            customerService.saveCustomerAndUpdateDetails(customerUpdateRequest.getCustomer(), customerUpdateRequest.getNewAddress());

            return new ResponseEntity<>("Customer created and details updated successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // Обработка ошибок
            return new ResponseEntity<>("Failed to create customer and update details: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "15. Успешное завершение или откат транзакции в " +
                    "зависимости от условий: создать метод, который добавляет запись в таблицу " +
                    "Transactions. Если сумма транзакции превышает лимит, откатить транзакцию. " +
                    "В противном случае – подтвердить изменения.\n"
    )
    @PostMapping("/transaction")
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
        try {
            transactionService.addTransaction(transaction);
            return new ResponseEntity<>("Transaction successfully added", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "16. Добавление пользователя без активной транзакции:" +
                    " создать метод, который добавляет нового пользователя в таблицу Users," +
                    " но не должен выполняться в рамках существующей транзакции. " +
                    "Использовать Propagation.NEVER. Если транзакция существует, выбрасывается исключение."
    )
    @PostMapping("/addUSerNever")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            userService.AddUSerWithoutTransaction(user);
            return new ResponseEntity<>("User added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "17. Обновление данных с повторной попыткой при конфликте:" +
                    " создать метод, который обновляет запись в таблице Products." +
                    " Если возникает конфликт (например, OptimisticLockException), " +
                    "метод должен повторить попытку обновления данных."
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        try {
            productService.updateProduct(product);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "18. Создание заказа и генерация инвойса в одной транзакции: " +
                    "создать метод, который создает новый заказ в таблице Orders и генерирует " +
                    "соответствующий инвойс в таблице Invoices внутри одной транзакции, используя " +
                    "TransactionTemplate. Если генерация инвойса не удается, транзакция должна быть откатана."
    )
    @PostMapping("/createOrderAndInvoice")
    public ResponseEntity<String> createOrderAndInvoice(
            @RequestBody OrderInvoice orderInvoice) {
        try {
            orderService.createOrderAndInvoice(orderInvoice.getOrder(), orderInvoice.getInvoice());
            return new ResponseEntity<>("Order and Invoice created successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create Order and Invoice: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "19. Обработка платежа с ограничением по времени: создать метод, который обрабатывает " +
                    "платеж и добавляет запись в таблицу Payments. Транзакция должна быть завершена в течение 5 секунд." +
                    " Если время превышено, транзакция откатывается."
    )
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody Payment payment) {
        try {
            paymentService.processPayment(payment);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment processing failed: " + e.getMessage());
        }
    }

    @Operation(
            summary = "20. Добавление и обновление с использованием `Propagation.REQUIRED`: создать метод, " +
                    "который добавляет нового клиента в таблицу Clients и обновляет данные о заказах клиента в " +
                    "таблице Orders. Метод должен использовать Propagation.REQUIRED, чтобы либо создать новую " +
                    "транзакцию, либо использовать существующую. Если обновление заказов не удается, транзакция " +
                    "должна быть откатана."
    )
    @PostMapping("/clients")
    public ResponseEntity<String> addClientAndOrders(@RequestBody Client client) {
        try {
            clientService.addClientAndUpdateOrders(client, client.getOrders());
            return ResponseEntity.ok("Client and orders added successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Failed to add client and orders: " + e.getMessage());
        }
    }
}

