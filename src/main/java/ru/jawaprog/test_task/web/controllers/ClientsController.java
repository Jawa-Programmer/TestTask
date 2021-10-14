package ru.jawaprog.test_task.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.web.entities.ClientDTO;
import ru.jawaprog.test_task.web.entities.ContractDTO;

import java.util.*;

@Tag(name = "Client", description = "Доступ к базе клиентов МТС")
@RestController
@RequestMapping("clients")
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable long id) {
        ClientDTO c = clientsService.get(id);
        if (c == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/contracts")
    public ResponseEntity<Collection<ContractDTO>> getClientsContracts(@PathVariable long id) {
        try {
            return new ResponseEntity<>(clientsService.getClientsContracts(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<Collection<ClientDTO>> findClients(@PathVariable String name) {
        return new ResponseEntity<>(clientsService.findByName(name), HttpStatus.OK);
    }

    @Operation(summary = "Возвращает всех клиентов", tags = "client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class)))
                    })
    })
    @GetMapping
    public ResponseEntity<Collection<ClientDTO>> getClients() {
        return new ResponseEntity<>(clientsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> postClient(@RequestParam(value = "fullName") String fullName,
                                                @RequestParam(value = "type") ClientDTO.ClientType type) {
        ClientDTO client = new ClientDTO();
        client.setFullName(fullName);
        client.setType(type);
        return new ResponseEntity<>(clientsService.saveNew(client), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> postClient(@PathVariable long id,
                                                @RequestParam(value = "fullName", required = false) String fullName,
                                                @RequestParam(value = "type", required = false) ClientDTO.ClientType type) {
        ClientDTO client = clientsService.update(id, fullName, type);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(client, HttpStatus.OK);
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
