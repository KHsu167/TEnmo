package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountBalance(String username) {
        String getBalanceSql = "SELECT account_id, a.user_id, balance" +
                " FROM account AS a" +
                " JOIN tenmo_user AS tu ON a.user_id = tu.user_id" +
                " WHERE username = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(getBalanceSql, username);
        if (result.next()) {
            return mapRowToAccount(result);
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        String getBalanceSql = "SELECT balance FROM account WHERE account_id = ?";
        BigDecimal result;
        result = jdbcTemplate.queryForObject(getBalanceSql, BigDecimal.class, accountId);
        return result;
    }


    @Override
    public void updateBalance(Long accountFromId, Long accountToId, BigDecimal amount) {
        BigDecimal accountFromBalance = getBalance(accountFromId);
        String updateBalanceSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(updateBalanceSql, accountFromBalance.subtract(amount), accountFromId);

        BigDecimal accountToBalance = getBalance(accountToId);
        jdbcTemplate.update(updateBalanceSql, accountToBalance.add(amount), accountToId);
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setId(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
