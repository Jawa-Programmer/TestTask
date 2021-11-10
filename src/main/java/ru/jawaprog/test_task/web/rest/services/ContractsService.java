package ru.jawaprog.test_task.web.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.rest.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.rest.services.mappers.AccountMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.ContractMapper;

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
        return ContractMapper.INSTANCE.fromDto(contractsRepository.findAll());
    }

    public Contract get(long id) {
        ContractDTO ct = contractsRepository.findById(id);
        if (ct == null)
            throw new NotFoundException(Contract.class);
        else
            return ContractMapper.INSTANCE.fromDto(ct);
    }

    public Contract saveNew(Contract c) {
        try {
            return ContractMapper.INSTANCE.fromDto(contractsRepository.insert(c.getNumber(), c.getClient().getId()));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException(Client.class);
        }
        return null;
    }

    public Contract update(long id, Long number, Long clientId) {
        try {
            ContractDTO ct = contractsRepository.update(id, number, clientId);
            if (ct == null)
                throw new NotFoundException(Contract.class);
            else
                return ContractMapper.INSTANCE.fromDto(ct);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException(Client.class);
        }
        return null;
    }

    public void delete(long id) {
        int ret = contractsRepository.deleteById(id);
        if (ret == 0) throw new NotFoundException(Contract.class);
    }

    public Collection<Account> getContractsAccounts(long id) {
        Collection<AccountDTO> accs = accountsRepository.findAccountsByContractId(id);
        return AccountMapper.INSTANCE.fromDto(accs);
    }
}
