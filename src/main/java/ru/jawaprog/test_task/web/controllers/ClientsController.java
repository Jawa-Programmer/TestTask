package ru.jawaprog.test_task.web.controllers;

import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;

import java.util.Collection;

@Log4j2
@Api(value = "База клиентов МТС", description = "RESTful сервис взаимодействия с БД клиентов МТС")
@RestController
@RequestMapping("clients")
public class ClientsController {
    @Autowired
    private ClientsService clientsService;

    private <T> ResponseEntity<T> logAndSend(ResponseEntity<T> response, WebRequest request) {
        log.info("Request: " + request + "; Response: " + response);
        return response;
    }

    @ApiOperation(value = "Получить клиента по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Client> getClient(
            WebRequest request,
            @ApiParam(value = "Идентификатор клиента", required = true, example = "1") @PathVariable long id
    ) {
        Client c = clientsService.get(id);
        return logAndSend(new ResponseEntity<>(c, HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список контрактов пользователя с данным id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @GetMapping(path = "/{id}/contracts")
    public ResponseEntity<Collection<Contract>> getClientsContracts(
            WebRequest request,
            @ApiParam(value = "Идентификатор клиента", required = true, example = "1") @PathVariable long id
    ) {
        return logAndSend(new ResponseEntity<>(clientsService.getClientsContracts(id), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список клиентов, в чьем имени содержится данная подстрока")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<Collection<Client>> findClients(
            WebRequest request,
            @ApiParam(value = "Фрагмент имени клиента", required = true) @PathVariable String name
    ) {
        return logAndSend(new ResponseEntity<>(clientsService.findByName(name), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список клиентов по номеру телефона")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/findByPhoneNumber/{number}")
    public ResponseEntity<Collection<Client>> findClientsByNumber(
            WebRequest request,
            @ApiParam(value = "Номер телефона", required = true) @PathVariable String number
    ) {
        return logAndSend(new ResponseEntity<>(clientsService.findByPhoneNumber(number), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список всех клиентов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<Client>> getClients(WebRequest request) {
        return logAndSend(new ResponseEntity<>(clientsService.findAll(), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Вносит нового клиента в базу данных")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен")
    })
    @PostMapping
    public ResponseEntity<Client> postClient(
            WebRequest request,
            @ApiParam(value = "Имя физ. лица или наименование организации", required = true) @RequestParam(value = "fullName") String fullName,
            @ApiParam(value = "Тип клиента", required = true) @RequestParam(value = "type") Client.ClientType type
    ) {
        Client client = new Client();
        client.setFullName(fullName);
        client.setType(type);
        return logAndSend(new ResponseEntity<>(clientsService.saveNew(client), HttpStatus.CREATED), request);
    }

    @ApiOperation(value = "Обновляет существующего клиента БД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Клиент не найден")})
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(
            WebRequest request,
            @ApiParam(value = "Идентификатор клиента", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Имя физ. лица или наименование организации") @RequestParam(value = "fullName", required = false) String fullName,
            @ApiParam(value = "Тип клиента") @RequestParam(value = "type", required = false) Client.ClientType type
    ) {
        Client client = clientsService.update(id, fullName, type);
        return logAndSend(new ResponseEntity<>(client, HttpStatus.OK), request);
    }

    @ApiOperation(value = "Удаляет клиента из БД по его id. Связанные с ним контракты и счета тоже удаляются")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(
            WebRequest request,
            @ApiParam(value = "Идентификатор клиента", required = true, example = "1") @PathVariable long id
    ) {
        clientsService.delete(id);
        return logAndSend(new ResponseEntity<>(HttpStatus.NO_CONTENT), request);
    }

}
