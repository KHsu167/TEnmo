package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer sendMoney(Transfer transfer, String username);

    List<Transfer> getListOfTransfersByUser(String username);

    Transfer getTransferByTransferId(Long transferId, String username);
}
