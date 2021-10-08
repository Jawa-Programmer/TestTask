package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.entities.Contract;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.services.ContractsService;

import java.util.List;

@RestController
@RequestMapping("contracts")
public class ContractsController {
    @Autowired
    private ContractsService contractsService;

    @Autowired
    private ClientsService clientsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable long id) {
        Contract c = contractsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Contract>> getContracts() {
        return new ResponseEntity<>(contractsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Contract> postContract(@RequestParam(value = "number") long contractNumber,
                                                 @RequestParam(value = "clientId") long clientId) {
        Client client = clientsService.get(clientId);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Contract contract = new Contract();
        contract.setNumber(contractNumber);
        contract.setClient(client);
        return new ResponseEntity<>(contractsService.save(contract), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Contract> putContract(@PathVariable long id, @RequestParam(value = "number", required = false) Long contractNumber,
                                                @RequestParam(value = "clientId", required = false) Long clientId) {
        Contract contract = contractsService.get(id);
        if (contract == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (clientId != null) {
            Client client = clientsService.get(clientId);
            if (client == null)
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            contract.setClient(client);
        }
        if (contractNumber != null)
            contract.setNumber(contractNumber);
        return new ResponseEntity<>(contractsService.save(contract), HttpStatus.OK);
    }

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
