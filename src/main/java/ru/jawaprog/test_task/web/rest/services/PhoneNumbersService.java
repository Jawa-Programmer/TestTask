package ru.jawaprog.test_task.web.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.rest.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.rest.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.rest.services.mappers.PhoneNumberMapper;

import java.util.Collection;
import java.util.List;

@Service
public class PhoneNumbersService {
    final private PhoneNumbersRepository phoneNumbersRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public PhoneNumbersService(PhoneNumbersRepository phoneNumbersRepository, AccountsRepository accountsRepository) {
        this.phoneNumbersRepository = phoneNumbersRepository;
        this.accountsRepository = accountsRepository;
    }

    public Collection<PhoneNumber> findAll() {
        List<PhoneNumberDTO> ret = phoneNumbersRepository.findAll();
        return PhoneNumberMapper.INSTANCE.fromDto(ret);
    }

    public Collection<PhoneNumber> getByNumber(String number) {
        return PhoneNumberMapper.INSTANCE.fromDto(phoneNumbersRepository.findAllByNumber(number));
    }

    public PhoneNumber get(long id) {
        PhoneNumberDTO phn = phoneNumbersRepository.findById(id);
        if (phn == null) throw new NotFoundException(PhoneNumber.class);
        return PhoneNumberMapper.INSTANCE.fromDto(phn);
    }

    public PhoneNumber saveNew(PhoneNumber num) {
        try {
            return PhoneNumberMapper.INSTANCE.fromDto(phoneNumbersRepository.insert(num.getNumber(), num.getAccount().getId()));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException(Account.class);
        }
        return null;
    }

    public PhoneNumber update(long id, String number, Long accountId) {
        try {
            PhoneNumberDTO phn = phoneNumbersRepository.update(id, number, accountId);
            if (phn == null) throw new NotFoundException(PhoneNumber.class);
            return PhoneNumberMapper.INSTANCE.fromDto(phn);
        } catch (
                DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException(Account.class);
        }
        return null;
    }

    public void delete(long id) {
        if (phoneNumbersRepository.deleteById(id) == 0) throw new NotFoundException(PhoneNumber.class);
    }
}
