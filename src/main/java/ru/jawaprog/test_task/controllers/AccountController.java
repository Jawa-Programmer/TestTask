package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Account getAccount(@PathVariable long id) {
        return accountsService.get(id);
    }

    @GetMapping
    public List<Account> getAccounts() {
        return accountsService.findAll();
    }

    @PostMapping
    public Account postAccount(@RequestParam(value = "number") int number,
                               @RequestParam(value = "contract_id") long contractId) {
        Contract contract = contractsService.get(contractId);
        if (contract == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract with id " + contractId + " Not Found");
        Account account = new Account();
        account.setContract(contract);
        account.setNumber(number);
        return accountsService.save(account);
    }

    @PutMapping(path = "/{id}")
    public Account putAccount(@PathVariable long id,
                              @RequestParam(value = "number", required = false) Integer number,
                              @RequestParam(value = "contract_id", required = false) Long contractId) {
        Account account = accountsService.get(id);
        if (account == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " Not Found");
        if (contractId != null) {
            Contract contract = contractsService.get(contractId);
            if (contract == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract with id " + contractId + " Not Found");
            account.setContract(contract);
        }
        if (number != null)
            account.setNumber(number);
        return accountsService.save(account);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAccount(@PathVariable long id) {
        try {
            accountsService.delete(id);
            throw new ResponseStatusException(HttpStatus.OK, "Account with id " + id + " was deleted");
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " Not Found");
        }
    }
}
