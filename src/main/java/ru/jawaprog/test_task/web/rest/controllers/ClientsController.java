package ru.jawaprog.test_task.web.rest.controllers;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.exceptions.InvalidParamsException;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.utils.Utils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;


@Api(value = "База клиентов МТС", description = "RESTful сервис взаимодействия с БД клиентов МТС")
@Validated
@RestController
@RequestMapping("clients")
public class ClientsController {
    final private ClientsService clientsService;
    private final Utils utils;

    @Autowired
    public ClientsController(ClientsService clientsService, Utils utils) {
        this.clientsService = clientsService;
        this.utils = utils;
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
        utils.validateId(id);
        Client c = clientsService.get(new Client(id, null, null));
        return utils.logAndSend(new ResponseEntity<>(c, HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список контрактов клиента с данным id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 404, message = "Клиент не найден")
    })
    @GetMapping(path = "/{id}/contracts")
    public ResponseEntity<Collection<Contract>> getClientsContracts(
            WebRequest request,
            @ApiParam(value = "Идентификатор клиента", required = true, example = "1") @PathVariable long id
    ) {
        utils.validateId(id);
        return utils.logAndSend(new ResponseEntity<>(clientsService.getClientsContracts(new Client(id, null, null)), HttpStatus.OK), request);
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
        return utils.logAndSend(new ResponseEntity<>(clientsService.findByName(new Client(null, name, null)), HttpStatus.OK), request);
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
        return utils.logAndSend(new ResponseEntity<>(clientsService.findByPhoneNumber(new PhoneNumber(number)), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Получить список всех клиентов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<Client>> getClients(WebRequest request) {
        return utils.logAndSend(new ResponseEntity<>(clientsService.findAllRest(), HttpStatus.OK), request);
    }

    @ApiOperation(value = "Вносит нового клиента в базу данных")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен")
    })
    @PostMapping
    public ResponseEntity<Client> postClient(
            WebRequest request,
            @ApiParam(value = "Имя физ. лица или наименование организации", required = true) @NotBlank @Size(min = 3) @RequestParam(value = "fullName") String fullName,
            @ApiParam(value = "Тип клиента", required = true) @RequestParam(value = "type") Client.ClientType type
    ) {
        Client client = new Client();
        client.setFullName(fullName);
        client.setType(type);
        return utils.logAndSend(new ResponseEntity<>(clientsService.saveNew(client), HttpStatus.CREATED), request);
    }

    @ApiOperation(value = "Обновляет существующего клиента БД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Клиент не найден")})
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(
            WebRequest request,
            @ApiParam(value = "Идентификатор клиента", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Имя физ. лица или наименование организации") @Size(min = 3) @RequestParam(value = "fullName", required = false) String fullName,
            @ApiParam(value = "Тип клиента") @RequestParam(value = "type", required = false) Client.ClientType type
    ) {
        utils.validateId(id);
        if (fullName != null && fullName.isBlank()) // пока так, ведь @NotBlank бракует null строки, хотя мне нужно их пропускать дальше
            throw new InvalidParamsException("updateClient.fullName: не должно быть пустым");
        Client client = clientsService.update(new Client(id, fullName, type));
        return utils.logAndSend(new ResponseEntity<>(client, HttpStatus.OK), request);
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
        utils.validateId(id);
        clientsService.delete(id);
        return utils.logAndSend(new ResponseEntity<>(HttpStatus.NO_CONTENT), request);
    }

}
