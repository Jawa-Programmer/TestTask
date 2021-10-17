package ru.jawaprog.test_task.web.controllers;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;

import java.util.Collection;

@Api(value = "База клиентов МТС", description = "RESTful сервис взаимодействия с БД клиентов МТС")
@RestController
@RequestMapping("clients")
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    @ApiOperation(value = "Получить клиента по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Client> getClient(
            @ApiParam(value = "Идентификатор клиента", required = true) @PathVariable long id
    ) {
        Client c = clientsService.get(id);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @ApiOperation(value = "Получить список контрактов пользователя с данным id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @GetMapping(path = "/{id}/contracts")
    public ResponseEntity<Collection<Contract>> getClientsContracts(
            @ApiParam(value = "Идентификатор клиента", required = true) @PathVariable long id
    ) {
        return new ResponseEntity<>(clientsService.getClientsContracts(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Получить список клиентов, в чьем имени содержится данная подстрока")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<Collection<Client>> findClients(
            @ApiParam(value = "Фрагмент имени клиента", required = true) @PathVariable String name
    ) {
        return new ResponseEntity<>(clientsService.findByName(name), HttpStatus.OK);
    }

    @ApiOperation(value = "Получить список клиентов по номеру телефона")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/findByPhoneNumber/{number}")
    public ResponseEntity<Collection<Client>> findClientsByNumber(
            @ApiParam(value = "Номер телефона", required = true) @PathVariable String number
    ) {
        return new ResponseEntity<>(clientsService.findByPhoneNumber(number), HttpStatus.OK);
    }

    @ApiOperation(value = "Получить список всех клиентов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<Client>> getClients() {
        return new ResponseEntity<>(clientsService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Вносит нового клиента в базу данных")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен")
    })
    @PostMapping
    public ResponseEntity<Client> postClient(
            @ApiParam(value = "Имя физ. лица или наименование организации", required = true) @RequestParam(value = "fullName") String fullName,
            @ApiParam(value = "Тип клиента", required = true) @RequestParam(value = "type") Client.ClientType type
    ) {
        Client client = new Client();
        client.setFullName(fullName);
        client.setType(type);
        return new ResponseEntity<>(clientsService.saveNew(client), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Обновляет существующего клиента БД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Клиент не найден")})
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(
            @ApiParam(value = "Идентификатор клиента", required = true) @PathVariable long id,
            @ApiParam(value = "Имя физ. лица или наименование организации") @RequestParam(value = "fullName", required = false) String fullName,
            @ApiParam(value = "Тип клиента") @RequestParam(value = "type", required = false) Client.ClientType type
    ) {
        Client client = clientsService.update(id, fullName, type);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @ApiOperation(value = "Удаляет клиента из БД по его id. Связанные с ним контракты и счета тоже удаляются")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(
            @ApiParam(value = "Идентификатор клиента", required = true) @PathVariable long id
    ) {
        clientsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
