package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer sendMoney(Transfer transfer);

    List<Transfer> getListOfTransfersByUser(String username);

    Transfer getTransferByTransferId(Long transferId);
}
