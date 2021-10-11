package ru.jawaprog.test_task.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.dao.entities.ContractDAO;
import ru.jawaprog.test_task.dao.services.ClientsService;
import ru.jawaprog.test_task.dao.services.ContractsService;
import ru.jawaprog.test_task.web.entities.ClientDTO;
import ru.jawaprog.test_task.web.entities.ContractDTO;

import java.util.List;

@RestController
@RequestMapping("contracts")
public class ContractsController {
    @Autowired
    private ContractsService contractsService;

    @Autowired
    private ClientsService clientsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<ContractDTO> getContract(@PathVariable long id) {
        ContractDTO c = contractsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }

    /*
        @GetMapping(path = "/{id}/accounts")
        public ResponseEntity<Collection<AccountDTO>> getContractAccounts(@PathVariable long id) {
            ContractDAO c = contractsService.get(id);
            if (c == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(c.getAccounts(), HttpStatus.OK);
        }
    */
    @GetMapping
    public ResponseEntity<List<ContractDTO>> getContracts() {
        return new ResponseEntity<>(contractsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContractDTO> postContract(@RequestParam(value = "number") long contractNumber,
                                                    @RequestParam(value = "clientId") long clientId) {
        ClientDTO client = new ClientDTO();
        client.setId(clientId);

        ContractDTO contract = new ContractDTO();
        contract.setNumber(contractNumber);
        contract.setClient(client);
        try {
            return new ResponseEntity<>(contractsService.saveNew(contract), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ContractDTO> putContract(@PathVariable long id, @RequestParam(value = "number", required = false) Long contractNumber,
                                                   @RequestParam(value = "clientId", required = false) Long clientId) {

        try {
            return new ResponseEntity<>(contractsService.update(id, contractNumber, clientId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
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
