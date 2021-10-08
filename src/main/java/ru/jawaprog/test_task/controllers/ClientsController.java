package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.services.ClientsService;

import java.util.List;

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
