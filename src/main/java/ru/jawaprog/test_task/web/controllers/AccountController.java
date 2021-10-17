package ru.jawaprog.test_task.web.controllers;


import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.services.ContractsService;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import java.util.Collection;

@RestController
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    private AccountsService accountsService;

    @Autowired
    private ContractsService contractsService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable long id) {
        Account acc = accountsService.get(id);
        if (acc == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}/phones")
    public ResponseEntity<Collection<PhoneNumber>> getAccountPhones(@PathVariable long id) {
        try {
            return new ResponseEntity<>(accountsService.getAccountsPhones(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<Account>> getAccounts() {
        return new ResponseEntity<>(accountsService.findAll(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Контракт с указанным contractId не найден")
    })
    @PostMapping
    public ResponseEntity<Account> postAccount(@RequestParam(value = "number") long number,
                                               @RequestParam(value = "contractId") long contractId) {
        Contract contract = new Contract();
        contract.setId(contractId);
        Account account = new Account();
        account.setContract(contract);
        account.setNumber(number);
        account = accountsService.saveNew(account);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        else
            return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Счёт не найден"),
            @ApiResponse(code = 422, message = "Контракт с указанным contractId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<Account> putAccount(@PathVariable long id,
                                              @RequestParam(value = "number", required = false) Integer number,
                                              @RequestParam(value = "contractId", required = false) Long contractId) {
        Account account;
        account = accountsService.update(id, number, contractId);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Счёт не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteAccount(@PathVariable long id) {
        accountsService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}