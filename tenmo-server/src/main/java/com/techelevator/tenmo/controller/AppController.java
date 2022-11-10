package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.IllegalUserRejectOrApproveException;
import com.techelevator.tenmo.exception.NotAPendingTransactionException;
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
    public Transfer transfer(@RequestBody Transfer transfer, @RequestParam String type_name, Principal principal) {
        String username = principal.getName();
        try {
           return transferDao.sendOrRequest(transfer, type_name, username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal Transfer or Request");
        }
    }

//        if (type_name == 1) {
//            try {
//                return transferDao.type_name(transfer, username);
//            } catch (IllegalArgumentException e) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal transfer.");
//            }
//        } else if (type_name == 2) {
//            try {
//                return transferDao.type_name(transfer, username);
//            } catch (IllegalArgumentException e) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request.");
//            }
//        } else
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal transfer.");

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

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/pending/{transferId}")
    public void rejectOrApprove(@PathVariable Long transferId, @RequestParam String status_name, Principal principal) {
        String username = principal.getName();
        try {
            transferDao.rejectOrApprove(transferId, status_name, username);
        } catch (IllegalUserRejectOrApproveException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is your own request!");
        } catch (NotAPendingTransactionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is not a pending request");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance to complete request");
        }
    }
}