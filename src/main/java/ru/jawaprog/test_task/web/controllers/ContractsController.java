package ru.jawaprog.test_task.web.controllers;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.ContractsService;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Api(value = "База контрактов МТС", description = "RESTful сервис взаимодействия с БД контрактов МТС")
@RestController
@RequestMapping("contracts")
public class ContractsController {
    @Autowired
    private ContractsService contractsService;

    @ExceptionHandler(value = {NoSuchElementException.class, EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleNotFound() {
        return new ResponseEntity<>("Контракт с переданным id не найден", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleConflict() {
        return new ResponseEntity<>("Нарушение ограничения внешнего ключа: клиент с переданным clientId не найден", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ApiOperation(value = "Получить контракт по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Contract> getContract(
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id
    ) {
        Contract c = contractsService.get(id);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }


    @ApiOperation(value = "Получить список счётов по по id контракта")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}/accounts")
    public ResponseEntity<Collection<Account>> getContractAccounts(
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id
    ) {
        return new ResponseEntity<>(contractsService.getContractsAccounts(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Получить список всех контрактов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<List<Contract>> getContracts() {
        return new ResponseEntity<>(contractsService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Добавить новый контракт в базу данных")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Клиент с указанным clientId не найден")
    })
    @PostMapping
    public ResponseEntity<Contract> postContract(
            @ApiParam(value = "Номер контракта", required = true) @RequestParam(value = "number") long contractNumber,
            @ApiParam(value = "id клиента. Внешний ключ", required = true) @RequestParam(value = "clientId") long clientId
    ) {
        Client client = new Client();
        client.setId(clientId);

        Contract contract = new Contract();
        contract.setNumber(contractNumber);
        contract.setClient(client);
        Contract nc = contractsService.saveNew(contract);
        return new ResponseEntity<>(nc, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Обновить существующий контракт по его id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Контракт не найден"),
            @ApiResponse(code = 422, message = "Клиент с указанным clientId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<Contract> putContract(
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Номер контракта") @RequestParam(value = "number", required = false) Long contractNumber,
            @ApiParam(value = "id клиента. Внешний ключ") @RequestParam(value = "clientId", required = false) Long clientId
    ) {
        return new ResponseEntity<>(contractsService.update(id, contractNumber, clientId), HttpStatus.OK);
    }
    @ApiOperation(value = "Удалить контракт по id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Контракт не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteContract(
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id
    ) {
        contractsService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
