package ru.jawaprog.test_task.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.web.entities.ClientDTO;
import ru.jawaprog.test_task.web.entities.ContractDTO;

import java.util.Collection;

@RestController
@RequestMapping("clients")
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable long id) {
        ClientDTO c = clientsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @GetMapping(path = "/{id}/contracts")
    public ResponseEntity<Collection<ContractDTO>> getClientsContracts(@PathVariable long id) {
        try {
            return new ResponseEntity<>(clientsService.getClientsContracts(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<Collection<ClientDTO>> findClients(@PathVariable String name) {
        return new ResponseEntity<>(clientsService.findByName(name), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<ClientDTO>> getClients() {
        return new ResponseEntity<>(clientsService.findAll(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен")
    })
    @PostMapping
    public ResponseEntity<ClientDTO> postClient(@RequestParam(value = "fullName") String fullName,
                                                @RequestParam(value = "type") ClientDTO.ClientType type) {
        ClientDTO client = new ClientDTO();
        client.setFullName(fullName);
        client.setType(type);
        return new ResponseEntity<>(clientsService.saveNew(client), HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Клиент не найден")})
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> postClient(@PathVariable long id,
                                                @RequestParam(value = "fullName", required = false) String fullName,
                                                @RequestParam(value = "type", required = false) ClientDTO.ClientType type) {
        ClientDTO client = clientsService.update(id, fullName, type);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
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
