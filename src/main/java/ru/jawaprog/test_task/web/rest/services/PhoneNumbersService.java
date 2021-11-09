package ru.jawaprog.test_task.web.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.web.rest.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.rest.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.rest.services.mappers.PhoneNumberMapper;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PhoneNumbersService {
    final private PhoneNumbersRepository phoneNumbersRepository;
    @Autowired
    private AccountsService accountsService;

    public PhoneNumbersService(PhoneNumbersRepository phoneNumbersRepository) {
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public Collection<PhoneNumber> findAll() {
        List<PhoneNumberDTO> ret = phoneNumbersRepository.findAll();
        return PhoneNumberMapper.INSTANCE.toDto(ret);
    }

    public Collection<PhoneNumber> getByNumber(String number) {
        return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.findAllByNumber(number));
    }

    public PhoneNumber get(long id) {
        try {
            return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(PhoneNumber.class);
        }
    }

    public PhoneNumber saveNew(PhoneNumber num) {
        AccountDTO acc = accountsService.getDao(num.getAccount().getId());
        if (acc == null) throw new ForeignKeyException(Account.class);
        PhoneNumberDTO toRet = new PhoneNumberDTO();
        toRet.setNumber(num.getNumber());
        toRet.setAccount(acc);
        return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.save(toRet));
    }

    public PhoneNumber update(long id, String number, Long accountId) {
        try {
            PhoneNumberDTO toRet = phoneNumbersRepository.findById(id).get();
            if (toRet == null) return null;
            if (accountId != null) {
                AccountDTO acc = accountsService.getDao(accountId);
                if (acc == null) throw new ForeignKeyException(Account.class);
                toRet.setAccount(acc);
            }
            if (number != null) toRet.setNumber(number);
            return PhoneNumberMapper.INSTANCE.toDto(toRet);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(PhoneNumber.class);
        }
    }

    public void delete(long id) {
        try {
            phoneNumbersRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(PhoneNumber.class);
        }
    }
}
