package ru.jawaprog.test_task.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.repositories.dao.AccountsDatabaseMapper;
import ru.jawaprog.test_task.repositories.dao.PhoneNumbersDatabaseMapper;
import ru.jawaprog.test_task.repositories.entities.AccountDTO;
import ru.jawaprog.test_task.repositories.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;

import java.util.Collection;
import java.util.List;

@Service
public class AccountsService {
    private final AccountsDatabaseMapper accountsDatabaseMapper;
    private final PhoneNumbersDatabaseMapper phoneNumbersDatabaseMapper;

    @Autowired
    public AccountsService(AccountsDatabaseMapper accountsDatabaseMapper, PhoneNumbersDatabaseMapper phoneNumbersDatabaseMapper) {
        this.accountsDatabaseMapper = accountsDatabaseMapper;
        this.phoneNumbersDatabaseMapper = phoneNumbersDatabaseMapper;
    }

    public Collection<Account> findAllRest() {
        return AccountMapper.INSTANCE.toRest(accountsDatabaseMapper.findAll());
    }

    public Account get(Account account) {
        AccountDTO acc = accountsDatabaseMapper.findById(account.getId());
        if (acc == null) throw new NotFoundException("Счёт");
        return AccountMapper.INSTANCE.toRest(acc);
    }

    public Account saveNew(Account acc) {
            return AccountMapper.INSTANCE.toRest(accountsDatabaseMapper.insert(AccountMapper.INSTANCE.toDto(acc)));
    }

    public Account update(Account account) {
        AccountDTO acc = accountsDatabaseMapper.update(AccountMapper.INSTANCE.toDto(account));
        if (acc == null) throw new NotFoundException("Счёт");
        return AccountMapper.INSTANCE.toRest(acc);
    }

    public Collection<PhoneNumber> getAccountsPhones(Account account) {
        if (!accountsDatabaseMapper.exists(account.getId()))
            throw new NotFoundException("Счёт");
        List<PhoneNumberDTO> phones = phoneNumbersDatabaseMapper.findByAccountId(account.getId());
        return PhoneNumberMapper.INSTANCE.toRest(phones);
    }


    public Collection<ru.jawaprog.test_task_mts.Account> findAllSoap() {
        return AccountMapper.INSTANCE.toSoap(accountsDatabaseMapper.findAll());
    }

    public ru.jawaprog.test_task_mts.Account get(ru.jawaprog.test_task_mts.Account account) {
        AccountDTO ret = accountsDatabaseMapper.findById(account.getId());
        if (ret == null) throw new NotFoundException("Счёт");
        return AccountMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.Account saveNew(ru.jawaprog.test_task_mts.Account account) {
        return AccountMapper.INSTANCE.toSoap(accountsDatabaseMapper.insert(AccountMapper.INSTANCE.toDto(account)));
    }

    public ru.jawaprog.test_task_mts.Account update(ru.jawaprog.test_task_mts.Account account) {

        AccountDTO ret = accountsDatabaseMapper.update(AccountMapper.INSTANCE.toDto(account));
        if (ret == null) throw new NotFoundException("Счёт");
        return AccountMapper.INSTANCE.toSoap(ret);

    }

    public void delete(long id) {
        if (accountsDatabaseMapper.deleteById(id) == 0)
            throw new NotFoundException("Счёт");
    }
}
