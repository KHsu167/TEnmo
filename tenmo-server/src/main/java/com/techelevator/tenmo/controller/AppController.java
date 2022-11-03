package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class AppController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AppController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/balance")
    public BigDecimal getBalance(Principal principal) {
        String username = principal.getName();
        return accountDao.getBalance(username);
    }

    //TODO need to only show user_id and username
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    //TODO
    @PostMapping(path = "/transfers")
    public Transfer sendMoney(@RequestBody Transfer transfer) {
        return null;
    }

    //TODO
    @GetMapping(path = "/transfers/{userId}")
    public List<Transfer> getAllTransfersByUserId(@PathVariable Long userId) {
        return null;
    }

    //TODO
    @GetMapping(path = "/transfers/{transfersId}")
    public Transfer getTransferDetailsByTransferId(@PathVariable Long transferId) {
        return null;
    }
}
