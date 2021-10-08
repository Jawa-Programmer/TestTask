package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jawaprog.test_task.entities.Account;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.entities.Contract;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.services.ContractsService;

import java.util.List;

@RestController
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    private AccountsService accountsService;

    @Autowired
    private ContractsService contractsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable long id) {
        Account acc = accountsService.get(id);
        if (acc == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return new ResponseEntity<>(accountsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Account> postAccount(@RequestParam(value = "number") int number,
                                               @RequestParam(value = "contractId") long contractId) {
        Contract contract = contractsService.get(contractId);
        if (contract == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Account account = new Account();
        account.setContract(contract);
        account.setNumber(number);
        return new ResponseEntity<>(accountsService.save(account), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Account> putAccount(@PathVariable long id,
                                              @RequestParam(value = "number", required = false) Integer number,
                                              @RequestParam(value = "contractId", required = false) Long contractId) {
        Account account = accountsService.get(id);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (contractId != null) {
            Contract contract = contractsService.get(contractId);
            if (contract == null)
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            account.setContract(contract);
        }
        if (number != null)
            account.setNumber(number);
        return new ResponseEntity<>(accountsService.save(account), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteAccount(@PathVariable long id) {
        try {
            accountsService.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
