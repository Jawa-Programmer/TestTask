package ru.jawaprog.test_task.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.dao.entities.AccountADO;
import ru.jawaprog.test_task.dao.entities.ContractDAO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDAO;
import ru.jawaprog.test_task.dao.services.AccountsService;
import ru.jawaprog.test_task.dao.services.ContractsService;

import java.util.Collection;
import java.util.List;
/*
@RestController
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    private AccountsService accountsService;

    @Autowired
    private ContractsService contractsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<AccountADO> getAccount(@PathVariable long id) {
        AccountADO acc = accountsService.get(id);
        if (acc == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/phones")
    public ResponseEntity<Collection<PhoneNumberDAO>> getAccountPhones(@PathVariable long id) {
        AccountADO acc = accountsService.get(id);
        if (acc == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(acc.getPhoneNumbers(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountADO>> getAccounts() {
        return new ResponseEntity<>(accountsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountADO> postAccount(@RequestParam(value = "number") int number,
                                                  @RequestParam(value = "contractId") long contractId) {
        ContractDAO contract = contractsService.get(contractId);
        if (contract == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        AccountADO account = new AccountADO();
        account.setContract(contract);
        account.setNumber(number);
        return new ResponseEntity<>(accountsService.save(account), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AccountADO> putAccount(@PathVariable long id,
                                                 @RequestParam(value = "number", required = false) Integer number,
                                                 @RequestParam(value = "contractId", required = false) Long contractId) {
        AccountADO account = accountsService.get(id);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (contractId != null) {
            ContractDAO contract = contractsService.get(contractId);
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
*/