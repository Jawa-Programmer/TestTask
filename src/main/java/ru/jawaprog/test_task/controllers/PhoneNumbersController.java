package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public PhoneNumber getPhoneNumber(@PathVariable long id) {
        return phoneNumbersService.get(id);
    }

    @GetMapping
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbersService.findAll();
    }

    @PostMapping
    public PhoneNumber postPhoneNumber(@RequestParam(value = "number") String number,
                                       @RequestParam(value = "account_id") long accountId) {
        Account account = accountsService.get(accountId);
        if (account == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + account + " Not Found");
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setAccount(account);
        phoneNumber.setNumber(number);
        return phoneNumbersService.save(phoneNumber);
    }

    @PutMapping(path = "/{id}")
    public PhoneNumber putPhoneNumber(@PathVariable long id,
                                       @RequestParam(value = "number", required = false) String number,
                                       @RequestParam(value = "account_id", required = false) Long accountId) {
        PhoneNumber phoneNumber = phoneNumbersService.get(id);
        if (phoneNumber == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone number with id " + id + " Not Found");

        if (accountId != null) {
            Account account = accountsService.get(accountId);
            if (account == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + accountId + " Not Found");
            phoneNumber.setAccount(account);
        }
        if (number != null)
            phoneNumber.setNumber(number);
        return phoneNumbersService.save(phoneNumber);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePhoneNumber(@PathVariable long id) {
        try {
            phoneNumbersService.delete(id);
            throw new ResponseStatusException(HttpStatus.OK, "Phone number with id " + id + " was deleted");
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone number with id " + id + " Not Found");
        }
    }
}
