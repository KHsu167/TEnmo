package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private UserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public Transfer sendMoney(Transfer transfer, String username) {

        Long accountFromId = transfer.getAccountFromId();
        Long accountToId = transfer.getAccountToId();
        BigDecimal transferAmount = transfer.getAmount();

        //check if accountFrom and accountTo are two different accounts
        if (accountFromId.equals(accountToId)) {
            throw new IllegalArgumentException();
        }

        //check if accountTo is an actual account
        if (userDao.findUserIdByAccountId(accountToId) == -1) {
            throw new IllegalArgumentException();
        }

        //check if transfer amount is greater than 0
        if (transferAmount.compareTo(new BigDecimal(0)) != 1) {
            throw new IllegalArgumentException();
        }

        //check if transfer amount is less than or equal to accountFrom balance
        if (transferAmount.compareTo(accountDao.getBalance(accountFromId)) == 1) {
            throw new IllegalArgumentException();
        }

        String sql = "INSERT INTO transfer (account_from_id, account_to_id, transfer_type_id," +
                " transfer_status_id, amount)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        //TODO create jdbc for status and type tables
        Long transferId = jdbcTemplate.queryForObject(sql, Long.class, transfer.getAccountFromId(),
                transfer.getAccountToId(), 1, 100, transfer.getAmount());

        accountDao.updateBalance(accountFromId, accountToId, transferAmount);

        //TODO used the transferId to get transfer detail
        return getTransferByTransferId(transferId, username);
    }

    @Override
    public List<Transfer> getListOfTransfersByUser(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, account_from_id, account_to_id," +
                " transfer_type_id, transfer_status_id, amount FROM transfer AS t" +
                " JOIN account AS a ON t.account_from_id = a.account_id" +
                " JOIN account AS a2 ON t.account_to_id = a2.account_id" +
                " JOIN tenmo_user AS tu ON a.user_id = tu.user_id OR a2.user_id = tu.user_id" +
                " WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(Long transferId, String username) {
        String sql = "SELECT transfer_id, account_from_id, account_to_id," +
                " transfer_type_id, transfer_status_id, amount FROM transfer AS t" +
                " JOIN account AS a ON t.account_from_id = a.account_id" +
                " JOIN account AS a2 ON t.account_to_id = a2.account_id" +
                " JOIN tenmo_user AS tu ON a.user_id = tu.user_id OR a2.user_id = tu.user_id" +
                " WHERE transfer_id = ? AND username = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId, username);
        if (result.next()) {
            return mapRowToTransfer(result);
        } else {
            return null;
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setAccountFromId(rs.getLong("account_from_id"));
        transfer.setAccountToId(rs.getLong("account_to_id"));
        transfer.setTransferTypeId(rs.getLong("transfer_type_id"));
        transfer.setTransferStatusId(rs.getLong("transfer_status_id"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
