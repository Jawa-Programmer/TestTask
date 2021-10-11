package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.entities.Account;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.entities.Contract;
import ru.jawaprog.test_task.entities.PhoneNumber;
import ru.jawaprog.test_task.services.ClientsService;

import java.util.*;

@RestController
@RequestMapping("clients")
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Client> getClient(@PathVariable long id) {
        Client c = clientsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/contracts")
    public ResponseEntity<Collection<Contract>> getClientsContracts(@PathVariable long id) {
        Client c = clientsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(c.getContracts(), HttpStatus.OK);
    }


    @GetMapping(path = "/{id}/phones")
    public ResponseEntity<Collection<PhoneNumber>> getClientsPhones(@PathVariable long id) {
        Client c = clientsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            Set<PhoneNumber> phones = new HashSet<>();
            for (Contract contract : c.getContracts())
                for (Account account : contract.getAccounts())
                    phones.addAll(account.getPhoneNumbers());
            return new ResponseEntity<>(phones, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<List<Client>> findClients(@PathVariable String name) {
        return new ResponseEntity<>(clientsService.findByName(name), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return new ResponseEntity<>(clientsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> postClient(@RequestParam(value = "fullName") String fullName,
                                             @RequestParam(value = "type") Client.ClientType type) {
        Client client = new Client();
        client.setFullName(fullName);
        client.setType(type);
        return new ResponseEntity<>(clientsService.save(client), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> postClient(@PathVariable long id,
                                             @RequestParam(value = "fullName", required = false) String fullName,
                                             @RequestParam(value = "type", required = false) Client.ClientType type) {
        Client client = clientsService.get(id);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (fullName != null)
            client.setFullName(fullName);
        if (type != null)
            client.setType(type);
        return new ResponseEntity<>(clientsService.save(client), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable long id) {
        try {
            clientsService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
