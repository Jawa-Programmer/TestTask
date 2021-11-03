package ru.jawaprog.test_task.web.soap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.rest.services.mappers.AccountMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapAccountMapper;
import ru.jawaprog.test_task_mts.Account;
import ru.jawaprog.test_task_mts.Contract;

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
        try {
            return SoapAccountMapper.INSTANCE.fromDto(accountsRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Account.class);
        }
    }


    public Account saveNew(long number, long contractId) {
        ContractDTO contract = contractsRepository.findById(contractId).orElse(null);
        if (contract == null) throw new ForeignKeyException(Contract.class);
        AccountDTO newAcc = new AccountDTO();
        newAcc.setContract(contract);
        newAcc.setNumber(number);
        return SoapAccountMapper.INSTANCE.fromDto(accountsRepository.save(newAcc));
    }

    public Account update(long id, Long number, Long contractId) {
        try {
            AccountDTO acc = accountsRepository.findById(id).get();
            if (contractId != null) {
                ContractDTO ct = contractsRepository.findById(contractId).orElse(null);
                if (ct == null) throw new ForeignKeyException(Contract.class);
                acc.setContract(ct);
            }
            if (number != null) acc.setNumber(number);
            return SoapAccountMapper.INSTANCE.fromDto(accountsRepository.save(acc));
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Account.class);
        }
    }

    public void delete(long id) {
        try {
            accountsRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(Account.class);
        }
    }
}
