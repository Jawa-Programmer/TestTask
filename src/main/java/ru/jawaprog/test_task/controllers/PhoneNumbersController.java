package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jawaprog.test_task.entities.Account;
import ru.jawaprog.test_task.entities.PhoneNumber;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.services.PhoneNumbersService;

import java.util.List;

@RestController
@RequestMapping("phone-numbers")
public class PhoneNumbersController {
    @Autowired
    private PhoneNumbersService phoneNumbersService;
    @Autowired
    private AccountsService accountsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<PhoneNumber> getPhoneNumber(@PathVariable long id) {
        PhoneNumber phoneNumber = phoneNumbersService.get(id);
        if (phoneNumber == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PhoneNumber>> getPhoneNumbers() {
        return new ResponseEntity<>(phoneNumbersService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PhoneNumber> postPhoneNumber(@RequestParam(value = "number") String number,
                                                       @RequestParam(value = "accountId") long accountId) {
        Account account = accountsService.get(accountId);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setAccount(account);
        phoneNumber.setNumber(number);
        return new ResponseEntity<>(phoneNumbersService.save(phoneNumber), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PhoneNumber> putPhoneNumber(@PathVariable long id,
                                                      @RequestParam(value = "number", required = false) String number,
                                                      @RequestParam(value = "accountId", required = false) Long accountId) {
        PhoneNumber phoneNumber = phoneNumbersService.get(id);
        if (phoneNumber == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (accountId != null) {
            Account account = accountsService.get(accountId);
            if (account == null)
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            phoneNumber.setAccount(account);
        }
        if (number != null)
            phoneNumber.setNumber(number);
        return new ResponseEntity<>(phoneNumbersService.save(phoneNumber), HttpStatus.OK);
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
