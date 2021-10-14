package ru.jawaprog.test_task.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.services.ContractsService;
import ru.jawaprog.test_task.web.entities.AccountDTO;
import ru.jawaprog.test_task.web.entities.ContractDTO;
import ru.jawaprog.test_task.web.entities.PhoneNumberDTO;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    private AccountsService accountsService;

    @Autowired
    private ContractsService contractsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable long id) {
        AccountDTO acc = accountsService.get(id);
        if (acc == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(acc, HttpStatus.OK);
    }


    @GetMapping(path = "/{id}/phones")
    public ResponseEntity<Collection<PhoneNumberDTO>> getAccountPhones(@PathVariable long id) {
        try {
            return new ResponseEntity<>(accountsService.getAccountsPhones(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<AccountDTO>> getAccounts() {
        return new ResponseEntity<>(accountsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountDTO> postAccount(@RequestParam(value = "number") long number,
                                                  @RequestParam(value = "contractId") long contractId) {
        ContractDTO contract = new ContractDTO();
        contract.setId(contractId);
        AccountDTO account = new AccountDTO();
        account.setContract(contract);
        account.setNumber(number);
        account = accountsService.saveNew(account);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        else
            return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AccountDTO> putAccount(@PathVariable long id,
                                                 @RequestParam(value = "number", required = false) Integer number,
                                                 @RequestParam(value = "contractId", required = false) Long contractId) {
        AccountDTO account;
        try {
            account = accountsService.update(id, number, contractId);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (account == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        else
            return new ResponseEntity<>(account, HttpStatus.CREATED);
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
