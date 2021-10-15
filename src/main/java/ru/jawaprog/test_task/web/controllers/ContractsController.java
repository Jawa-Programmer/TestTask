package ru.jawaprog.test_task.web.controllers;


import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.services.ContractsService;
import ru.jawaprog.test_task.web.entities.AccountDTO;
import ru.jawaprog.test_task.web.entities.ClientDTO;
import ru.jawaprog.test_task.web.entities.ContractDTO;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("contracts")
public class ContractsController {
    @Autowired
    private ContractsService contractsService;

    @Autowired
    private ClientsService clientsService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<ContractDTO> getContract(@PathVariable long id) {
        ContractDTO c = contractsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}/accounts")
    public ResponseEntity<Collection<AccountDTO>> getContractAccounts(@PathVariable long id) {
        try {
            return new ResponseEntity<>(contractsService.getContractsAccounts(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<List<ContractDTO>> getContracts() {
        return new ResponseEntity<>(contractsService.findAll(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Клиент с указанным clientId не найден")
    })
    @PostMapping
    public ResponseEntity<ContractDTO> postContract(@RequestParam(value = "number") long contractNumber,
                                                    @RequestParam(value = "clientId") long clientId) {
        ClientDTO client = new ClientDTO();
        client.setId(clientId);

        ContractDTO contract = new ContractDTO();
        contract.setNumber(contractNumber);
        contract.setClient(client);
        ContractDTO nc = contractsService.saveNew(contract);
        if (nc != null) {
            return new ResponseEntity<>(nc, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Контракт не найден"),
            @ApiResponse(code = 422, message = "Клиент с указанным clientId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<ContractDTO> putContract(@PathVariable long id, @RequestParam(value = "number", required = false) Long contractNumber,
                                                   @RequestParam(value = "clientId", required = false) Long clientId) {

        try {
            return new ResponseEntity<>(contractsService.update(id, contractNumber, clientId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Контракт не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteContract(@PathVariable long id) {
        try {
            contractsService.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
