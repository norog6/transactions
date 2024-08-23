package com.softclub.trans.controller;

import com.softclub.trans.DTO.CustomerUpdateRequest;
import com.softclub.trans.DTO.OrderInvoice;
import com.softclub.trans.entity.*;
import com.softclub.trans.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    @PostMapping("/addUSer")
    public String AddUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return "Пользователь успешно добавлен.";
        } catch (Exception ex) {
            return "Ошибка при добавлении пользователя: " + ex.getMessage();
        }

    }

    @PostMapping("/balance")
    public ResponseEntity<String> updateBalance(@RequestBody Accounts accounts) {
        try {
            accountService.updateAccountBalance(accounts.getName(), accounts.getBalance());
            return ResponseEntity.ok("Balance updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public String createOrderWithPayment(@RequestBody Order order) {
        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setOrderId(order.getId());
        orderService.createOrderWithPayment(order, payment);
        return "Order and payment processed";
    }
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<String> addReview(@PathVariable Long productId, @RequestBody Review review) {

        try {
            productService.addReviewAndUpdateProduct(productId, review);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Product updated");
    }
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
    @GetMapping("/orders/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
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
    @PostMapping("/update")
    public String updateInventory(@RequestParam Long id, @RequestParam int newStock) {
        inventoryService.updateInventory(id, newStock);
        return "Inventory updated successfully!";
    }
    @PostMapping("/sup/create-clean")
    public String createAndCleanupSuppliers(@RequestBody Supplier supplier) {
        supplierService.createAndCleanupSuppliers(supplier);
        return "Supplier created and old records for this supplier cleaned up!";
    }
    @GetMapping("/read-uncommitted")
    public List<RawData> readUncommittedData() {
        return rawDataService.readUncommittedData();
    }
    @PutMapping("/{orderId}/update-order-amount")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam Double newAmount) {
        orderService.updateOrderStatus(orderId, newAmount);
        return "Order status amount successfully.";
    }
    @PutMapping("/{userProfileId}/updateUserProfile")
    public String updateUserProfile(@PathVariable Long userProfileId, @RequestBody UserProfile userProfile) {
        userProfileService.updateUserProfile(userProfileId,userProfile);
        return "User profile updated successfully";
    }
    @DeleteMapping("/delete-sessions")
    public String deleteSessions() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);  // Устаревшие сессии старше 7 дней
        Date expirationTime = calendar.getTime();

        sessionService.removeExpiredSessions(expirationTime);
        return "Sessions delete succesfuly";
    }
    @PostMapping("/customer")
    public ResponseEntity<String> createCustomerAndUpdateDetails(@RequestBody CustomerUpdateRequest customerUpdateRequest) {
        try {

            customerService.saveCustomerAndUpdateDetails(customerUpdateRequest.getCustomer(),customerUpdateRequest.getNewAddress());

            return new ResponseEntity<>("Customer created and details updated successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // Обработка ошибок
            return new ResponseEntity<>("Failed to create customer and update details: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
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
    @PostMapping("/addUSerNested")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return new ResponseEntity<>("User added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
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
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody Payment payment) {
        try {
            paymentService.processPayment(payment);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment processing failed: " + e.getMessage());
        }
    }
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

