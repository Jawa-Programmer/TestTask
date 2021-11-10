package ru.jawaprog.test_task.web.soap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapAccountMapper;
import ru.jawaprog.test_task_mts.Account;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class AccountsSoapService {
    private final AccountsRepository accountsRepository;
    private final ContractsRepository contractsRepository;

    @Autowired
    public AccountsSoapService(AccountsRepository accountsRepository, ContractsRepository contractsRepository) {
        this.accountsRepository = accountsRepository;
        this.contractsRepository = contractsRepository;
    }

    public Collection<Account> findAll() {
        return SoapAccountMapper.INSTANCE.fromDto(accountsRepository.findAll());
    }

    public Account get(long id) {
        AccountDTO ret = accountsRepository.findById(id);
        if (ret == null) throw new NotFoundException("Счёт");
        return SoapAccountMapper.INSTANCE.fromDto(ret);
    }

    public Account saveNew(long number, long contractId) {
        try {
            return SoapAccountMapper.INSTANCE.fromDto(accountsRepository.insert(number, contractId));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException("Контракт");
        }
        return null;
    }

    public Account update(long id, Long number, Long contractId) {
        try {
            AccountDTO ret = accountsRepository.update(id, number, contractId);
            if (ret == null) throw new NotFoundException("Счёт");
            return SoapAccountMapper.INSTANCE.fromDto(ret);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException("Контракт");
        }
        return null;
    }

    public void delete(long id) {
        if (accountsRepository.deleteById(id) == 0) {
            throw new NotFoundException("Счёт");
        }
    }
}
