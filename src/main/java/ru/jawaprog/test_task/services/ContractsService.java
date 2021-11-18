package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Contract;

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

    public Collection<Contract> findAllRest() {
        return ContractMapper.INSTANCE.toRest(contractsRepository.findAll());
    }

    public Contract get(Contract c) {
        ContractDTO ct = contractsRepository.findById(c.getId());
        if (ct == null)
            throw new NotFoundException("Контракт");
        else
            return ContractMapper.INSTANCE.toRest(ct);
    }

    public Contract saveNew(Contract c) {
        try {
            return ContractMapper.INSTANCE.toRest(
                    contractsRepository.insert(
                            ContractMapper.INSTANCE.toDto(c)
                    )
            );
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Клиент");
        }
        return null;
    }

    public Contract update(Contract c) {
        try {
            ContractDTO ct = contractsRepository.update(ContractMapper.INSTANCE.toDto(c));
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


    public Collection<Account> getContractsAccounts(Contract c) {
        Collection<AccountDTO> accs = accountsRepository.findAccountsByContractId(c.getId());
        return AccountMapper.INSTANCE.toRest(accs);
    }

    public Collection<ru.jawaprog.test_task_mts.Contract> findAllSoap() {
        return ContractMapper.INSTANCE.toSoap(contractsRepository.findAll());
    }

    public ru.jawaprog.test_task_mts.Contract get(ru.jawaprog.test_task_mts.Contract contract) {
        ContractDTO ret = contractsRepository.findById(contract.getId());
        if (ret == null)
            throw new NotFoundException("Контракт");
        return ContractMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.Contract saveNew(ru.jawaprog.test_task_mts.Contract contract) {
        try {
            ContractDTO ret = contractsRepository.insert(ContractMapper.INSTANCE.toDto(contract));
            return ContractMapper.INSTANCE.toSoap(ret);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Клиент");
        }
        return null;
    }

    public ru.jawaprog.test_task_mts.Contract update(ru.jawaprog.test_task_mts.Contract contract) {
        try {
            ContractDTO ret = contractsRepository.update(ContractMapper.INSTANCE.toDto(contract));
            if (ret == null)
                throw new NotFoundException("Контракт");
            return ContractMapper.INSTANCE.toSoap(ret);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Клиент");
        }
        return null;
    }

    public void delete(long id) {
        if (contractsRepository.deleteById(id) == 0) {
            throw new NotFoundException("Контракт");
        }
    }

}
