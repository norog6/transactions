package com.softclub.trans.controller;

import com.softclub.trans.entity.Accounts;
import com.softclub.trans.entity.User;
import com.softclub.trans.service.AccountService;
import com.softclub.trans.service.UserService;
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
}
