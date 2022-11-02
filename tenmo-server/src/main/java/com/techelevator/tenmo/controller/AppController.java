package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class AppController {

    private AccountDao accountDao;

    public AppController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping(path = "/balance")
    public BigDecimal getBalance(Principal principal) {
        String username = principal.getName();
        return accountDao.getBalance(username);
    }
}
