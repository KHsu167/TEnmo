package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(String username) {
        String getBalanceSql = "SELECT balance" +
                " FROM account AS a" +
                " JOIN tenmo_user AS tu ON a.user_id = tu.user_id" +
                " WHERE username = ?";
        BigDecimal currentBalance;

        currentBalance = jdbcTemplate.queryForObject(getBalanceSql, BigDecimal.class, username);
        return currentBalance;
    }
}
