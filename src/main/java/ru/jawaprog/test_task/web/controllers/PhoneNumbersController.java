package ru.jawaprog.test_task.web.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.services.PhoneNumbersService;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import java.util.Collection;
import java.util.NoSuchElementException;

@Api(value = "База номеров телефоном МТС", description = "RESTful сервис взаимодействия с БД номеров МТС")
@RestController
@RequestMapping("phone-numbers")
public class PhoneNumbersController {
    @Autowired
    private PhoneNumbersService phoneNumbersService;

    @ExceptionHandler(value = {NoSuchElementException.class, EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleNotFound() {
        return new ResponseEntity<>("Телефонный номер с переданным id не найден", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleConflict() {
        return new ResponseEntity<>("Нарушение ограничения внешнего ключа: лицевой счёт с переданным accountId не найден", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<PhoneNumber> getPhoneNumber(
            @ApiParam(value = "Идентификатор номера телефона", required = true, example = "1") @PathVariable long id
    ) {
        PhoneNumber phoneNumber = phoneNumbersService.get(id);
        return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<PhoneNumber>> getPhoneNumbers() {
        return new ResponseEntity<>(phoneNumbersService.findAll(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Счет с указанным accountId не найден")
    })
    @PostMapping
    public ResponseEntity<PhoneNumber> postPhoneNumber(
            @ApiParam(value = "Номер телефона", required = true) @RequestParam(value = "number") String number,
            @ApiParam(value = "id лицевого счёта. Внешний ключ", required = true) @RequestParam(value = "accountId") long accountId
    ) {
        Account account = new Account();
        account.setId(accountId);

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setAccount(account);
        phoneNumber.setNumber(number);
        phoneNumber = phoneNumbersService.saveNew(phoneNumber);

        return new ResponseEntity<>(phoneNumber, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Телефон не найден"),
            @ApiResponse(code = 422, message = "Счет с указанным accountId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<PhoneNumber> putPhoneNumber(
            @ApiParam(value = "Идентификатор номера телефона", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Номер телефона") @RequestParam(value = "number", required = false) String number,
            @ApiParam(value = "id лицевого счёта. Внешний ключ") @RequestParam(value = "accountId", required = false) Long accountId
    ) {
        PhoneNumber phoneNumber;
        phoneNumber = phoneNumbersService.update(id, number, accountId);
        return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Телефон не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deletePhoneNumber(
            @ApiParam(value = "Идентификатор номера телефона", required = true, example = "1") @PathVariable long id
    ) {
        phoneNumbersService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }
}
