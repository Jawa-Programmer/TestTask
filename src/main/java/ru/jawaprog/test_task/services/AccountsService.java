package ru.jawaprog.test_task.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import java.util.Collection;

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
        return AccountMapper.INSTANCE.toDto(accountsRepository.findById(id).get());
    }

    AccountDTO getDao(long id) {
        return accountsRepository.findById(id).orElse(null);
    }

    public Account saveNew(Account acc) {
        ContractDTO contract = contractsService.getDAO(acc.getContract().getId());
        if (contract == null) throw new IllegalArgumentException();
        AccountDTO newAcc = new AccountDTO();
        newAcc.setContract(contract);
        newAcc.setNumber(acc.getNumber());
        return AccountMapper.INSTANCE.toDto(accountsRepository.save(newAcc));
    }

    public Account update(long id, Integer number, Long contractId) {
        AccountDTO acc = accountsRepository.findById(id).get();
        if (contractId != null) {
            ContractDTO ct = contractsService.getDAO(contractId);
            if (ct == null) throw new IllegalArgumentException();
            acc.setContract(ct);
        }
        if (number != null) acc.setNumber(number);
        return AccountMapper.INSTANCE.toDto(accountsRepository.save(acc));
    }

    public void delete(long id) {
        accountsRepository.deleteById(id);
    }

    public Collection<PhoneNumber> getAccountsPhones(long id) {
        AccountDTO accountDTO = accountsRepository.findById(id).get();
        return PhoneNumberMapper.INSTANCE.toDto(accountDTO.getPhoneNumbers());
    }
}
