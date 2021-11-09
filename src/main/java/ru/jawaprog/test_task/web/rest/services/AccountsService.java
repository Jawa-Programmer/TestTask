package ru.jawaprog.test_task.web.rest.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.web.rest.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.rest.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.rest.services.mappers.AccountMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.PhoneNumberMapper;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class AccountsService {
    private final AccountsRepository accountsRepository;
    @Autowired
    private ContractsService contractsService;

    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Collection<Account> findAll() {
        return AccountMapper.INSTANCE.toDto(accountsRepository.findAll());
    }

    public Account get(long id) {
        try {
            return AccountMapper.INSTANCE.toDto(accountsRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Account.class);
        }
    }

    AccountDTO getDao(long id) {
        return accountsRepository.findById(id).orElse(null);
    }

    public Account saveNew(Account acc) {
        ContractDTO contract = contractsService.getDAO(acc.getContract().getId());
        if (contract == null) throw new ForeignKeyException(Contract.class);
        AccountDTO newAcc = new AccountDTO();
        newAcc.setContract(contract);
        newAcc.setNumber(acc.getNumber());
        return AccountMapper.INSTANCE.toDto(accountsRepository.save(newAcc));
    }

    public Account update(long id, Integer number, Long contractId) {
        try {
            AccountDTO acc = accountsRepository.findById(id).get();
            if (contractId != null) {
                ContractDTO ct = contractsService.getDAO(contractId);
                if (ct == null) throw new ForeignKeyException(Contract.class);
                acc.setContract(ct);
            }
            if (number != null) acc.setNumber(number);
            return AccountMapper.INSTANCE.toDto(accountsRepository.save(acc));
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

    public Collection<PhoneNumber> getAccountsPhones(long id) {
        AccountDTO accountDTO = accountsRepository.findById(id).get();
        return PhoneNumberMapper.INSTANCE.toDto(accountDTO.getPhoneNumbers());
    }
}
