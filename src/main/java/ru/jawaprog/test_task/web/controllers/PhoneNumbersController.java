package ru.jawaprog.test_task.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDAO;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.services.PhoneNumbersService;
import ru.jawaprog.test_task.web.entities.AccountDTO;
import ru.jawaprog.test_task.web.entities.PhoneNumberDTO;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("phone-numbers")
public class PhoneNumbersController {
    @Autowired
    private PhoneNumbersService phoneNumbersService;
    @Autowired
    private AccountsService accountsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<PhoneNumberDTO> getPhoneNumber(@PathVariable long id) {
        PhoneNumberDTO phoneNumber = phoneNumbersService.get(id);
        if (phoneNumber == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<PhoneNumberDTO>> getPhoneNumbers() {
        return new ResponseEntity<>(phoneNumbersService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PhoneNumberDTO> postPhoneNumber(@RequestParam(value = "number") String number,
                                                          @RequestParam(value = "accountId") long accountId) {
        AccountDTO account = new AccountDTO();
        account.setId(accountId);

        PhoneNumberDTO phoneNumber = new PhoneNumberDTO();
        phoneNumber.setAccount(account);
        phoneNumber.setNumber(number);
        phoneNumber = phoneNumbersService.saveNew(phoneNumber);
        if (phoneNumber == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        else
            return new ResponseEntity<>(phoneNumber, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PhoneNumberDTO> putPhoneNumber(@PathVariable long id,
                                                         @RequestParam(value = "number", required = false) String number,
                                                         @RequestParam(value = "accountId", required = false) Long accountId) {
        PhoneNumberDTO phoneNumber;
        try {
            phoneNumber = phoneNumbersService.update(id, number, accountId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (phoneNumber == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deletePhoneNumber(@PathVariable long id) {
        try {
            phoneNumbersService.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
