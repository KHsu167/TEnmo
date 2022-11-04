package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountBalance(String username);

    BigDecimal getBalance(Long accountId);

    void updateBalance(Long accountFromId, Long accountToId, BigDecimal amount);
}
