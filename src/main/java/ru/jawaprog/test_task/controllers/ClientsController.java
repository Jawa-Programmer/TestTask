package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.services.ClientsService;

import java.util.List;

@RestController
@RequestMapping("clients")
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @GetMapping(path = "/{id}")
    public Client getClient(@PathVariable long id) {
        return clientsService.get(id);
    }

    @GetMapping
    public List<Client> getClients() {
        return clientsService.findAll();
    }

    @PostMapping
    public Client postClient(@RequestParam(value = "full_name") String fullName,
                             @RequestParam(value = "type") Client.ClientType type) {
        Client client = new Client();
        client.setFullName(fullName);
        client.setType(type);
        return clientsService.save(client);
    }

    @PutMapping("/{id}")
    public Client postClient(@PathVariable long id,
                             @RequestParam(value = "full_name", required = false) String fullName,
                             @RequestParam(value = "type", required = false) Client.ClientType type) {
        Client client = clientsService.get(id);
        if (client == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " Not Found");
        if (fullName != null)
            client.setFullName(fullName);
        if (type != null)
            client.setType(type);
        return clientsService.save(client);
    }

    @DeleteMapping("/{id}")
    public void postClient(@PathVariable long id) {
        try {
            clientsService.delete(id);
            throw new ResponseStatusException(HttpStatus.OK, "Client with id " + id + " was deleted");
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " Not Found");
        }
    }

}
