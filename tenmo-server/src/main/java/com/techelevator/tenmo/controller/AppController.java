package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class AppController {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public AppController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @GetMapping(path = "/balance")
    public Account getBalance(Principal principal) {
        String username = principal.getName();
        Account account = accountDao.getAccountBalance(username);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found.");
        } else {
            return account;
        }
    }

    @GetMapping(path = "/users")
    public List<UserDTO> getAllUsers(Principal principal) {
        String username = principal.getName();
        return userDao.findAll(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/transfer")
    public Transfer sendMoney(@RequestBody Transfer transfer, Principal principal, @RequestParam int transferTypeId ) {
        String username = principal.getName();
        if (transferTypeId == 1) {
            try {
                return transferDao.sendMoney(transfer, username);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal transfer.");
            }
        } else if (transferTypeId == 2) {
            try {
                return transferDao.requestMoney(transfer, username);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request.");
            }
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal transfer.");
    }

    @GetMapping(path = "/all-transfer")
    public List<Transfer> getAllTransfersByUser(Principal principal) {
        String username = principal.getName();
        List<Transfer> transfers = transferDao.getListOfTransfersByUser(username);
        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers found");
        }
        return transfers;
    }

    @GetMapping(path = "/transfers/{transferId}")
    public Transfer getTransferDetailsByTransferId(@PathVariable Long transferId, Principal principal) {
        String username = principal.getName();
        Transfer transfer = transferDao.getTransferByTransferId(transferId, username);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        }
        return transfer;
    }


}
