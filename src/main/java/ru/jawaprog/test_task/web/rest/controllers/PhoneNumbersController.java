package ru.jawaprog.test_task.web.rest.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.rest.services.PhoneNumbersService;
import ru.jawaprog.test_task.web.rest.utils.Utils;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Collection;

@Api(value = "База номеров телефоном МТС", description = "RESTful сервис взаимодействия с БД номеров МТС")
@RestController
@Validated
@RequestMapping("phone-numbers")
public class PhoneNumbersController {
    private final PhoneNumbersService phoneNumbersService;
    private final Utils utils;

    @Autowired
    public PhoneNumbersController(PhoneNumbersService phoneNumbersService, Utils utils) {
        this.phoneNumbersService = phoneNumbersService;
        this.utils = utils;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<PhoneNumber> getPhoneNumber(
            WebRequest request,
            @ApiParam(value = "Идентификатор номера телефона", required = true, example = "1") @PathVariable long id
    ) {
        utils.validateId(id);
        PhoneNumber phoneNumber = phoneNumbersService.get(id);
        return utils.logAndSend(new ResponseEntity<>(phoneNumber, HttpStatus.OK), request);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Collection<PhoneNumber>> getPhoneNumbers(WebRequest request) {
        return utils.logAndSend(new ResponseEntity<>(phoneNumbersService.findAll(), HttpStatus.OK), request);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Успешно добавлен"),
            @ApiResponse(code = 422, message = "Счет с указанным accountId не найден")
    })
    @PostMapping
    public ResponseEntity<PhoneNumber> postPhoneNumber(
            WebRequest request,
            @ApiParam(value = "Номер телефона", required = true) @Pattern(regexp = "\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}") @RequestParam(value = "number") String number,
            @ApiParam(value = "id лицевого счёта. Внешний ключ", required = true) @Min(1) @RequestParam(value = "accountId") long accountId
    ) {
        Account account = new Account();
        account.setId(accountId);

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setAccount(account);
        phoneNumber.setNumber(number);
        phoneNumber = phoneNumbersService.saveNew(phoneNumber);

        return utils.logAndSend(new ResponseEntity<>(phoneNumber, HttpStatus.CREATED), request);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно обновлен"),
            @ApiResponse(code = 404, message = "Телефон не найден"),
            @ApiResponse(code = 422, message = "Счет с указанным accountId не найден")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<PhoneNumber> putPhoneNumber(
            WebRequest request,
            @ApiParam(value = "Идентификатор номера телефона", required = true, example = "1") @PathVariable long id,
            @ApiParam(value = "Номер телефона") @Pattern(regexp = "\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}") @RequestParam(value = "number", required = false) String number,
            @ApiParam(value = "id лицевого счёта. Внешний ключ") @Min(1) @RequestParam(value = "accountId", required = false) Long accountId
    ) {
        utils.validateId(id);
        PhoneNumber phoneNumber;
        phoneNumber = phoneNumbersService.update(id, number, accountId);
        return utils.logAndSend(new ResponseEntity<>(phoneNumber, HttpStatus.OK), request);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Успешно удален"),
            @ApiResponse(code = 404, message = "Телефон не найден")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deletePhoneNumber(
            WebRequest request,
            @ApiParam(value = "Идентификатор номера телефона", required = true, example = "1") @PathVariable long id
    ) {
        utils.validateId(id);
        phoneNumbersService.delete(id);
        return utils.logAndSend(new ResponseEntity(HttpStatus.NO_CONTENT), request);
    }
}
