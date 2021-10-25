package ru.jawaprog.test_task.web.controllers;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.exceptions.InvalidParamsException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

import static ru.jawaprog.test_task.web.utils.Utils.logAndSend;
import static ru.jawaprog.test_task.web.utils.Utils.validateId;

@Api(value = "База клиентов МТС", description = "RESTful сервис взаимодействия с БД клиентов МТС")
@Validated
@RestController
@RequestMapping("clients")
public class ClientsController {
    final private ClientsService clientsService;

    @Autowired
    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
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
        validateId(id);
        Client c = clientsService.get(id);
        return logAndSend(new ResponseEntity<>(c, HttpStatus.OK), request);
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
        validateId(id);
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
            @ApiParam(value = "Имя физ. лица или наименование организации", required = true) @NotBlank @Size(min = 3) @RequestParam(value = "fullName") String fullName,
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
            @ApiParam(value = "Имя физ. лица или наименование организации") @Size(min = 3) @RequestParam(value = "fullName", required = false) String fullName,
            @ApiParam(value = "Тип клиента") @RequestParam(value = "type", required = false) Client.ClientType type
    ) {
        validateId(id);
        if (fullName != null && fullName.isBlank()) // пока так, ведь @NotBlank бракует null строки, хотя мне нужно их пропускать дальше
            throw new InvalidParamsException("updateClient.fullName: не должно быть пустым");
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
        validateId(id);
        clientsService.delete(id);
        return logAndSend(new ResponseEntity<>(HttpStatus.NO_CONTENT), request);
    }

}
