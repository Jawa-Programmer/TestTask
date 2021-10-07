package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Contract getContract(@PathVariable long id) {
        return contractsService.get(id);
    }

    @GetMapping
    public List<Contract> getContracts() {
        return contractsService.findAll();
    }

    @PostMapping
    public Contract postContract(@RequestParam(value = "contract_number") long contractNumber,
                                 @RequestParam(value = "client_id") long clientId) {
        Client client = clientsService.get(clientId);
        if (client == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + clientId + " Not Found");
        Contract contract = new Contract();
        contract.setContractNumber(contractNumber);
        contract.setClient(client);
        return contractsService.save(contract);
    }

    @PutMapping(path = "/{id}")
    public Contract putContract(@PathVariable long id, @RequestParam(value = "contract_number", required = false) Long contractNumber,
                                @RequestParam(value = "client_id", required = false) Long clientId) {
        Contract contract = contractsService.get(id);
        if (contract == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract with id " + id + " Not Found");
        if (clientId != null) {
            Client client = clientsService.get(clientId);
            if (client == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + clientId + " Not Found");
            contract.setClient(client);
        }
        if (contractNumber != null)
            contract.setContractNumber(contractNumber);
        return contractsService.save(contract);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteContract(@PathVariable long id) {
        try {
            contractsService.delete(id);
            throw new ResponseStatusException(HttpStatus.OK, "Contract with id " + id + " was deleted");
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract with id " + id + " Not Found", ex);
        }
    }
}
