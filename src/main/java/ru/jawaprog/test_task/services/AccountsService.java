package ru.jawaprog.test_task.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;

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

    public Collection<Account> findAllRest() {
        return AccountMapper.INSTANCE.toRest(accountsRepository.findAll());
    }

    public Account get(Account account) {
        AccountDTO acc = accountsRepository.findById(account.getId());
        if (acc == null) throw new NotFoundException("Счёт");
        return AccountMapper.INSTANCE.toRest(acc);
    }

    public Account saveNew(Account acc) {
        try {
            return AccountMapper.INSTANCE.toRest(accountsRepository.insert(AccountMapper.INSTANCE.toDto(acc)));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Контракт");
        }
        return null;
    }

    public Account update(Account account) {
        try {
            AccountDTO acc = accountsRepository.update(AccountMapper.INSTANCE.toDto(account));
            if (acc == null) throw new NotFoundException("Счёт");
            return AccountMapper.INSTANCE.toRest(acc);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Контракт");
        }
        return null;
    }

    public Collection<PhoneNumber> getAccountsPhones(Account account) {
        List<PhoneNumberDTO> phones = phoneNumbersRepository.findByAccountId(account.getId());
        return PhoneNumberMapper.INSTANCE.toRest(phones);
    }


    public Collection<ru.jawaprog.test_task_mts.Account> findAllSoap() {
        return AccountMapper.INSTANCE.toSoap(accountsRepository.findAll());
    }

    public ru.jawaprog.test_task_mts.Account get(ru.jawaprog.test_task_mts.Account account) {
        AccountDTO ret = accountsRepository.findById(account.getId());
        if (ret == null) throw new NotFoundException("Счёт");
        return AccountMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.Account saveNew(ru.jawaprog.test_task_mts.Account account) {
        try {
            return AccountMapper.INSTANCE.toSoap(accountsRepository.insert(AccountMapper.INSTANCE.toDto(account)));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Контракт");
        }
        return null;
    }

    public ru.jawaprog.test_task_mts.Account update(ru.jawaprog.test_task_mts.Account account) {
        try {
            AccountDTO ret = accountsRepository.update(AccountMapper.INSTANCE.toDto(account));
            if (ret == null) throw new NotFoundException("Счёт");
            return AccountMapper.INSTANCE.toSoap(ret);
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
}
