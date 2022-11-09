package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer sendOrRequest(Transfer transfer, String typeName, String username);

    Transfer requestMoney(Transfer transfer, String username);

    void rejectOrApprove(Long transferId, String statusName, String username);

    List<Transfer> getListOfTransfersByUser(String username);

    Transfer getTransferByTransferId(Long transferId, String username);
}
