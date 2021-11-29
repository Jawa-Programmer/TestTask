package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.repositories.dao.AccountsDatabaseMapper;
import ru.jawaprog.test_task.repositories.dao.ContractsDatabaseMapper;
import ru.jawaprog.test_task.repositories.entities.AccountDTO;
import ru.jawaprog.test_task.repositories.entities.ContractDTO;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Contract;

import java.util.Collection;

@Service
public class ContractsService {

    private final ContractsDatabaseMapper contractsDatabaseMapper;
    private final AccountsDatabaseMapper accountsDatabaseMapper;

    @Autowired
    public ContractsService(ContractsDatabaseMapper contractsDatabaseMapper, AccountsDatabaseMapper accountsDatabaseMapper) {
        this.contractsDatabaseMapper = contractsDatabaseMapper;
        this.accountsDatabaseMapper = accountsDatabaseMapper;
    }

    public Collection<Contract> findAllRest() {
        return ContractMapper.INSTANCE.toRest(contractsDatabaseMapper.findAll());
    }

    public Contract get(Contract c) {
        ContractDTO ct = contractsDatabaseMapper.findById(c.getId());
        if (ct == null)
            throw new NotFoundException("Контракт");
        else
            return ContractMapper.INSTANCE.toRest(ct);
    }

    public Contract saveNew(Contract c) {
        return ContractMapper.INSTANCE.toRest(
                contractsDatabaseMapper.insert(
                        ContractMapper.INSTANCE.toDto(c)
                )
        );
    }

    public Contract update(Contract c) {
        ContractDTO ct = contractsDatabaseMapper.update(ContractMapper.INSTANCE.toDto(c));
        if (ct == null)
            throw new NotFoundException("Контракт");
        else
            return ContractMapper.INSTANCE.toRest(ct);

    }


    public Collection<Account> getContractsAccounts(Contract c) {
        if (!contractsDatabaseMapper.exists(c.getId()))
            throw new NotFoundException("Контракт");
        Collection<AccountDTO> accs = accountsDatabaseMapper.findAccountsByContractId(c.getId());
        return AccountMapper.INSTANCE.toRest(accs);
    }

    public Collection<ru.jawaprog.test_task_mts.Contract> findAllSoap() {
        return ContractMapper.INSTANCE.toSoap(contractsDatabaseMapper.findAll());
    }

    public ru.jawaprog.test_task_mts.Contract get(ru.jawaprog.test_task_mts.Contract contract) {
        ContractDTO ret = contractsDatabaseMapper.findById(contract.getId());
        if (ret == null)
            throw new NotFoundException("Контракт");
        return ContractMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.Contract saveNew(ru.jawaprog.test_task_mts.Contract contract) {
        ContractDTO ret = contractsDatabaseMapper.insert(ContractMapper.INSTANCE.toDto(contract));
        return ContractMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.Contract update(ru.jawaprog.test_task_mts.Contract contract) {
        ContractDTO ret = contractsDatabaseMapper.update(ContractMapper.INSTANCE.toDto(contract));
        if (ret == null)
            throw new NotFoundException("Контракт");
        return ContractMapper.INSTANCE.toSoap(ret);
    }

    public void delete(long id) {
        if (contractsDatabaseMapper.deleteById(id) == 0) {
            throw new NotFoundException("Контракт");
        }
    }

}
