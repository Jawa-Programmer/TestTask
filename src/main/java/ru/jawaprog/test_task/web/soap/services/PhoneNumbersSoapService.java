package ru.jawaprog.test_task.web.soap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapAccountMapper;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapPhoneNumberMapper;
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
        PhoneNumberDTO ret = phoneNumbersRepository.findById(id);
        if (ret == null)
            throw new NotFoundException("Номер телефона");

        return SoapPhoneNumberMapper.INSTANCE.fromDto(ret);
    }

    public PhoneNumber saveNew(String number, long accountId) {
        try {
            return SoapPhoneNumberMapper.INSTANCE.fromDto(phoneNumbersRepository.insert(number, accountId));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException("Счёт");
        }
        return null;
    }

    public PhoneNumber update(long id, String number, Long accountId) {
        try {
            PhoneNumberDTO ret = phoneNumbersRepository.update(id, number, accountId);
            if (ret == null)
                throw new NotFoundException("Номер телефона");
            return SoapPhoneNumberMapper.INSTANCE.fromDto(ret);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException("Счёт");
        }
        return null;
    }

    public void delete(long id) {
        if (phoneNumbersRepository.deleteById(id) == 0) {
            throw new NotFoundException("Номер телефона");
        }
    }
}
