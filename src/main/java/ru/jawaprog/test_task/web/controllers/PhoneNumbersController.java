package ru.jawaprog.test_task.web.controllers;

/*
@RestController
@RequestMapping("phone-numbers")
public class PhoneNumbersController {
    @Autowired
    private PhoneNumbersService phoneNumbersService;
    @Autowired
    private AccountsService accountsService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<PhoneNumberDAO> getPhoneNumber(@PathVariable long id) {
        PhoneNumberDAO phoneNumber = phoneNumbersService.get(id);
        if (phoneNumber == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PhoneNumberDAO>> getPhoneNumbers() {
        return new ResponseEntity<>(phoneNumbersService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PhoneNumberDAO> postPhoneNumber(@RequestParam(value = "number") String number,
                                                          @RequestParam(value = "accountId") long accountId) {
        AccountADO account = accountsService.get(accountId);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        PhoneNumberDAO phoneNumber = new PhoneNumberDAO();
        phoneNumber.setAccount(account);
        phoneNumber.setNumber(number);
        return new ResponseEntity<>(phoneNumbersService.save(phoneNumber), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PhoneNumberDAO> putPhoneNumber(@PathVariable long id,
                                                         @RequestParam(value = "number", required = false) String number,
                                                         @RequestParam(value = "accountId", required = false) Long accountId) {
        PhoneNumberDAO phoneNumber = phoneNumbersService.get(id);
        if (phoneNumber == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (accountId != null) {
            AccountADO account = accountsService.get(accountId);
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
*/