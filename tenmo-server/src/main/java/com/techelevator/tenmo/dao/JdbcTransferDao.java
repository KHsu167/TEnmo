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

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer sendMoney(Transfer transfer) {

        Long accountFromId = transfer.getAccountFromId();
        Long accountToId = transfer.getAccountToId();

        if (accountFromId.equals(accountToId)) {
            return null;
            //TODO maybe throw an exception
        }

        if (transfer.getAmount().compareTo(new BigDecimal(0)) != 1) {
            return null;
            //TODO maybe throw an exception
        }

        String sql = "INSERT INTO transfer (account_from_id, account_to_id, transfer_type_id," +
                " transfer_status_id, amount)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        Long transferId = jdbcTemplate.queryForObject(sql, Long.class, transfer.getAccountFromId(),
                transfer.getAccountToId(), transfer.getTransferTypeId(),
                transfer.getTransferStatusId(), transfer.getAmount());

        //TODO used the transferId to get transfer detail
        return getTransferByTransferId(transferId);
    }

    @Override
    public List<Transfer> getListOfTransfersByUser(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, account_from_id, account_to_id," +
                " transfer_type_id, transfer_status_id, amount FROM transfer AS t" +
                " JOIN account AS a ON t.account_from_id = a.account_id" +
                " JOIN tenmo_user AS tu ON a.user_id = tu.user_id" +
                " WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(Long transferId) {
        String sql = "SELECT transfer_id, account_from_id, account_to_id," +
                " transfer_type_id, transfer_status_id, amount FROM transfer" +
                " WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        if (result.next()) {
            return mapRowToTransfer(result);
        } else {

            //TODO exception?
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
