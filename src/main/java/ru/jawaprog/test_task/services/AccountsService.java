package ru.jawaprog.test_task.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDAO;
import ru.jawaprog.test_task.dao.entities.ContractDAO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.entities.AccountDTO;
import ru.jawaprog.test_task.web.entities.PhoneNumberDTO;

import java.util.Collection;

@Service
public class AccountsService {
    private final AccountsRepository accountsRepository;
    @Autowired
    private ContractsService contractsService;

    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Collection<AccountDTO> findAll() {
        return AccountMapper.INSTANCE.toDto(accountsRepository.findAll());
    }

    public AccountDTO get(long id) {
        return AccountMapper.INSTANCE.toDto(accountsRepository.findById(id).orElse(null));
    }

    AccountDAO getDao(long id) {
        return accountsRepository.findById(id).orElse(null);
    }

    public AccountDTO saveNew(AccountDTO acc) {
        ContractDAO contract = contractsService.getDAO(acc.getContract().getId());
        if (contract == null) return null;
        AccountDAO newAcc = new AccountDAO();
        newAcc.setContract(contract);
        newAcc.setNumber(acc.getNumber());
        return AccountMapper.INSTANCE.toDto(accountsRepository.save(newAcc));
    }

    public AccountDTO update(long id, Integer number, Long contractId) throws Exception {
        AccountDAO acc = accountsRepository.findById(id).orElse(null);
        if (acc == null) return null;
        ContractDAO contract = contractsService.getDAO(contractId);
        if (contractId != null) {
            ContractDAO ct = contractsService.getDAO(contractId);
            if (ct == null) throw new Exception();
            acc.setContract(ct);
        }
        if (number != null) acc.setNumber(number);
        return AccountMapper.INSTANCE.toDto(accountsRepository.save(acc));
    }

    public void delete(long id) {
        accountsRepository.deleteById(id);
    }

    public Collection<PhoneNumberDTO> getAccountsPhones(long id) throws Exception {
        AccountDAO accountDAO = accountsRepository.findById(id).orElse(null);
        if (accountDAO == null) throw new Exception();
        else return PhoneNumberMapper.INSTANCE.toDto(accountDAO.getPhoneNumbers());
    }
}
