package ru.jawaprog.test_task.web.soap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapPhoneNumberMapper;
import ru.jawaprog.test_task_mts.Account;
import ru.jawaprog.test_task_mts.PhoneNumber;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PhoneNumbersSoapService {

    private final PhoneNumbersRepository phoneNumbersRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public PhoneNumbersSoapService(PhoneNumbersRepository phoneNumbersRepository, AccountsRepository accountsRepository) {
        this.phoneNumbersRepository = phoneNumbersRepository;
        this.accountsRepository = accountsRepository;
    }

    public Collection<PhoneNumber> findAll() {
        List<PhoneNumberDTO> ret = phoneNumbersRepository.findAll();
        return SoapPhoneNumberMapper.INSTANCE.fromDto(ret);
    }

    public Collection<PhoneNumber> getByNumber(String number) {
        return SoapPhoneNumberMapper.INSTANCE.fromDto(phoneNumbersRepository.findAllByNumber(number));
    }

    public PhoneNumber get(long id) {
        try {
            return SoapPhoneNumberMapper.INSTANCE.fromDto(phoneNumbersRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("Номер телефона");
        }
    }

    public PhoneNumber saveNew(String number, long accountId) {
        AccountDTO acc = accountsRepository.findById(accountId).orElse(null);
        if (acc == null) throw new ForeignKeyException("Счёт");
        PhoneNumberDTO toRet = new PhoneNumberDTO();
        toRet.setNumber(number);
        toRet.setAccount(acc);
        return SoapPhoneNumberMapper.INSTANCE.fromDto(phoneNumbersRepository.save(toRet));
    }

    public PhoneNumber update(long id, String number, Long accountId) {
        try {
            PhoneNumberDTO toRet = phoneNumbersRepository.findById(id).get();
            if (toRet == null) return null;
            if (accountId != null) {
                AccountDTO acc = accountsRepository.findById(accountId).orElse(null);
                if (acc == null) throw new ForeignKeyException("Счёт");
                toRet.setAccount(acc);
            }
            if (number != null) toRet.setNumber(number);
            return SoapPhoneNumberMapper.INSTANCE.fromDto(toRet);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("Номер телефона");
        }
    }

    public void delete(long id) {
        try {
            phoneNumbersRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Номер телефона");
        }
    }
}
