package ru.jawaprog.test_task.web.controllers;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import java.util.Collection;
import java.util.NoSuchElementException;

@Api(value = "База лицевых счетов МТС", description = "RESTful сервис взаимодействия с БД счетов МТС")
@RestController
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    private AccountsService accountsService;

    @ExceptionHandler(value = {NoSuchElementException.class, EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleNotFound() {
        return new ResponseEntity<>("Лицевой счёт с переданным id не найден", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleConflict() {
        return new ResponseEntity<>("Нарушение ограничения внешнего ключа: контракт с переданным contractId не найден", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ApiOperation(value = "Получить лицевой счет по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccount(
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id
    ) {
        Account acc = accountsService.get(id);
        if (acc == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @ApiOperation(value = "Получить список телефонов по id лицевого счета")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}/phones")
    public ResponseEntity<Collection<PhoneNumber>> getAccountPhones(
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id
    ) {
        try {
            return new ResponseEntity<>(accountsService.getAccountsPhones(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Получить список лицевых счетов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<Account>> getAccounts() {
        return new ResponseEntity<>(accountsService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Занести новый счёт в базу данных")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Контракт с указанным contractId не найден")
    })
    @PostMapping
    public ResponseEntity<Account> postAccount(
            @ApiParam(value = "Номер лицевого счёта", required = true) @RequestParam(value = "number") long number,
            @ApiParam(value = "Номер контракта. Внешний ключ", required = true) @RequestParam(value = "contractId") long contractId) {
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

    @ApiOperation(value = "Обновить существующий лицевой счет по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Счёт не найден"),
            @ApiResponse(code = 422, message = "Контракт с указанным contractId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<Account> putAccount(
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Номер лицевого счёта") @RequestParam(value = "number", required = false) Integer number,
            @ApiParam(value = "Номер контракта. Внешний ключ") @RequestParam(value = "contractId", required = false) Long contractId
    ) {
        Account account;
        account = accountsService.update(id, number, contractId);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Удалить лицевой счет по id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Счёт не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteAccount(
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id
    ) {
        accountsService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
