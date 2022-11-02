package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Long transferId;
    private Long accountFromId;
    private Long accountToId;
    private Long transferTypeId;
    private Long transferStatusId;
    private BigDecimal amount;

    public Transfer() {
    }

    public Transfer(Long transferId, Long accountFromId, Long accountToId, Long transferTypeId, Long transferStatusId, BigDecimal amount) {
        this.transferId = transferId;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.amount = amount;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(Long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(Long accountToId) {
        this.accountToId = accountToId;
    }

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
