package ru.jawaprog.test_task.web.rest.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.rest.services.mappers.AccountMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;

import java.util.Collection;
import java.util.List;

@Service
public class AccountsService {
    private final AccountsRepository accountsRepository;
    private final PhoneNumbersRepository phoneNumbersRepository;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository, PhoneNumbersRepository phoneNumbersRepository) {
        this.accountsRepository = accountsRepository;
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public Collection<Account> findAll() {
        return AccountMapper.INSTANCE.toRest(accountsRepository.findAll());
    }

    public Account get(long id) {
        AccountDTO acc = accountsRepository.findById(id);
        if (acc == null) throw new NotFoundException("Счёт");
        return AccountMapper.INSTANCE.toRest(acc);
    }

    public Account saveNew(Account acc) {
        try {
            return AccountMapper.INSTANCE.toRest(accountsRepository.insert(acc.getNumber(), acc.getContract().getId()));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Контракт");
        }
        return null;
    }

    public Account update(long id, Long number, Long contractId) {
        try {
            AccountDTO acc = accountsRepository.update(id, number, contractId);
            if (acc == null) throw new NotFoundException("Счёт");
            return AccountMapper.INSTANCE.toRest(acc);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Контракт");
        }
        return null;
    }

    public void delete(long id) {
        if (accountsRepository.deleteById(id) == 0)
            throw new NotFoundException("Счёт");
    }

    public Collection<PhoneNumber> getAccountsPhones(long id) {
        List<PhoneNumberDTO> phones = phoneNumbersRepository.findByAccountId(id);
        return PhoneNumberMapper.INSTANCE.toRest(phones);
    }


}
