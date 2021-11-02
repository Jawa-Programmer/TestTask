package ru.jawaprog.test_task.web.rest.controllers;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.web.rest.services.ContractsService;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.utils.Utils;

import javax.validation.constraints.Min;
import java.util.Collection;

@Api(value = "База контрактов МТС", description = "RESTful сервис взаимодействия с БД контрактов МТС")
@Validated
@RestController
@RequestMapping("contracts")
public class ContractsController {
    private final ContractsService contractsService;
    private final Utils utils;

    @Autowired
    public ContractsController(ContractsService contractsService, Utils utils) {
        this.contractsService = contractsService;
        this.utils = utils;
    }

    @ApiOperation(value = "Получить контракт по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Contract> getContract(
            WebRequest request,
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id
    ) {
        utils.validateId(id);
        Contract c = contractsService.get(id);
        return utils.logAndSend(new ResponseEntity<>(c, HttpStatus.OK), request);
    }


    @ApiOperation(value = "Получить список счётов по по id контракта")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}/accounts")
    public ResponseEntity<Collection<Account>> getContractAccounts(
            WebRequest request,
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id
    ) {
        utils.validateId(id);
        return utils.logAndSend(new ResponseEntity<>(contractsService.getContractsAccounts(id), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список всех контрактов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<Contract>> getContracts(WebRequest request) {
        return new ResponseEntity<>(contractsService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Добавить новый контракт в базу данных")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Клиент с указанным clientId не найден")
    })
    @PostMapping
    public ResponseEntity<Contract> postContract(
            WebRequest request,
            @ApiParam(value = "Номер контракта", required = true) @Min(0) @RequestParam(value = "number") long contractNumber,
            @ApiParam(value = "id клиента. Внешний ключ", required = true) @Min(1) @RequestParam(value = "clientId") long clientId
    ) {
        Client client = new Client();
        client.setId(clientId);

        Contract contract = new Contract();
        contract.setNumber(contractNumber);
        contract.setClient(client);
        Contract nc = contractsService.saveNew(contract);
        return utils.logAndSend(new ResponseEntity<>(nc, HttpStatus.CREATED), request);
    }

    @ApiOperation(value = "Обновить существующий контракт по его id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Контракт не найден"),
            @ApiResponse(code = 422, message = "Клиент с указанным clientId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<Contract> putContract(
            WebRequest request,
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Номер контракта") @Min(0) @RequestParam(value = "number", required = false) Long contractNumber,
            @ApiParam(value = "id клиента. Внешний ключ") @Min(1) @RequestParam(value = "clientId", required = false) Long clientId
    ) {
        utils.validateId(id);
        return utils.logAndSend(new ResponseEntity<>(contractsService.update(id, contractNumber, clientId), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Удалить контракт по id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Контракт не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteContract(
            WebRequest request,
            @ApiParam(value = "Идентификатор контракта", required = true, example = "1") @PathVariable long id
    ) {
        utils.validateId(id);
        contractsService.delete(id);
        return utils.logAndSend(new ResponseEntity(HttpStatus.NO_CONTENT), request);
    }
}
