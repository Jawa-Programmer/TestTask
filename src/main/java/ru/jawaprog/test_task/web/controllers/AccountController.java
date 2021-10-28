package ru.jawaprog.test_task.web.controllers;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import javax.validation.constraints.Min;
import java.util.Collection;

import static ru.jawaprog.test_task.web.utils.Utils.logAndSend;
import static ru.jawaprog.test_task.web.utils.Utils.validateId;

@Api(value = "База лицевых счетов МТС", description = "RESTful сервис взаимодействия с БД счетов МТС")
@RestController
@Validated
@RequestMapping("accounts")
public class AccountController {
    private final AccountsService accountsService;

    @Autowired
    public AccountController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @ApiOperation(value = "Получить лицевой счет по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccount(
            WebRequest request,
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id
    ) {
        validateId(id);
        Account acc = accountsService.get(id);
        return logAndSend(new ResponseEntity<>(acc, HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список телефонов по id лицевого счета")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}/phones")
    public ResponseEntity<Collection<PhoneNumber>> getAccountPhones(
            WebRequest request,
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id
    ) {
        validateId(id);
        return logAndSend(new ResponseEntity<>(accountsService.getAccountsPhones(id), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список лицевых счетов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<Account>> getAccounts(WebRequest request) {
        return logAndSend(new ResponseEntity<>(accountsService.findAll(), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Занести новый счёт в базу данных")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Контракт с указанным contractId не найден")
    })
    @PostMapping
    public ResponseEntity<Account> postAccount(
            WebRequest request,
            @ApiParam(value = "Номер лицевого счёта", required = true) @Min(0) @RequestParam(value = "number") long number,
            @ApiParam(value = "Номер контракта. Внешний ключ", required = true) @Min(1) @RequestParam(value = "contractId") long contractId) {
        Contract contract = new Contract();
        contract.setId(contractId);
        Account account = new Account();
        account.setContract(contract);
        account.setNumber(number);
        account = accountsService.saveNew(account);
        return logAndSend(new ResponseEntity<>(account, HttpStatus.CREATED), request);
    }

    @ApiOperation(value = "Обновить существующий лицевой счет по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Счёт не найден"),
            @ApiResponse(code = 422, message = "Контракт с указанным contractId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<Account> putAccount(
            WebRequest request,
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Номер лицевого счёта") @Min(0) @RequestParam(value = "number", required = false) Integer number,
            @ApiParam(value = "Номер контракта. Внешний ключ") @Min(1) @RequestParam(value = "contractId", required = false) Long contractId
    ) {
        validateId(id);
        return logAndSend(new ResponseEntity<>(accountsService.update(id, number, contractId), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Удалить лицевой счет по id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Счёт не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteAccount(
            WebRequest request,
            @ApiParam(value = "Идентификатор лицевого счёта", required = true, example = "1") @PathVariable long id
    ) {
        validateId(id);
        accountsService.delete(id);
        return logAndSend(new ResponseEntity(HttpStatus.NO_CONTENT), request);
    }
}
