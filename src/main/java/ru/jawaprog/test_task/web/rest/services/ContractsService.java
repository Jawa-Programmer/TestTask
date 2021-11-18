package ru.jawaprog.test_task.web.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.services.mappers.AccountMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;

import java.util.Collection;

@Service
public class ContractsService {

    private final ContractsRepository contractsRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public ContractsService(ContractsRepository contractsRepository, AccountsRepository accountsRepository) {
        this.contractsRepository = contractsRepository;
        this.accountsRepository = accountsRepository;
    }

    public Collection<Contract> findAll() {
        return ContractMapper.INSTANCE.toRest(contractsRepository.findAll());
    }

    public Contract get(long id) {
        ContractDTO ct = contractsRepository.findById(id);
        if (ct == null)
            throw new NotFoundException("Контракт");
        else
            return ContractMapper.INSTANCE.toRest(ct);
    }

    public Contract saveNew(Contract c) {
        try {
            return ContractMapper.INSTANCE.toRest(contractsRepository.insert(c.getNumber(), c.getClient().getId()));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Клиент");
        }
        return null;
    }

    public Contract update(long id, Long number, Long clientId) {
        try {
            ContractDTO ct = contractsRepository.update(id, number, clientId);
            if (ct == null)
                throw new NotFoundException("Контракт");
            else
                return ContractMapper.INSTANCE.toRest(ct);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Клиент");
        }
        return null;
    }

    public void delete(long id) {
        int ret = contractsRepository.deleteById(id);
        if (ret == 0) throw new NotFoundException("Контракт");
    }

    public Collection<Account> getContractsAccounts(long id) {
        Collection<AccountDTO> accs = accountsRepository.findAccountsByContractId(id);
        return AccountMapper.INSTANCE.toRest(accs);
    }
}
