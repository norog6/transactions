package com.softclub.trans.controller;

import com.softclub.trans.entity.Accounts;
import com.softclub.trans.entity.Order;
import com.softclub.trans.entity.Payment;
import com.softclub.trans.entity.User;
import com.softclub.trans.service.AccountService;
import com.softclub.trans.service.OrderService;
import com.softclub.trans.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;


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
    @RestController
    public class OrderController {

        @Autowired
        private OrderService orderService;

        @PostMapping("/create")
        public String createOrderWithPayment(@RequestBody OrderRequest orderRequest) {
            Order order = new Order();
            order.setDescription(orderRequest.getOrderDescription());

            Payment payment = new Payment();
            payment.setAmount(orderRequest.getPaymentAmount());
            payment.setOrderId(order.getId());

            orderService.createOrderWithPayment(order, payment);

            return "Order and payment processed";
        }
    }
@Getter
@Setter
@RequiredArgsConstructor
    class OrderRequest {
        private String orderDescription;
        private Double paymentAmount;
    }
}
