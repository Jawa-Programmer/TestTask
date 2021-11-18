package ru.jawaprog.test_task.web.soap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.rest.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task_mts.Contract;

import java.util.Collection;

@Service
public class ContractsSoapService {

    private final ContractsRepository contractsRepository;


    @Autowired
    public ContractsSoapService(ContractsRepository contractsRepository) {
        this.contractsRepository = contractsRepository;
    }

    public Collection<Contract> findAll() {
        return ContractMapper.INSTANCE.toSoap(contractsRepository.findAll());
    }

    public Contract get(long id) {
        ContractDTO ret = contractsRepository.findById(id);
        if (ret == null)
            throw new NotFoundException("Контракт");
        return ContractMapper.INSTANCE.toSoap(ret);
    }

    public Contract saveNew(long number, long clientId) {
        try {
            ContractDTO ret = contractsRepository.insert(number, clientId);
            return ContractMapper.INSTANCE.toSoap(ret);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException("Клиент");
        }
        return null;
    }

    public Contract update(long id, Long number, Long clientId) {
        try {
            ContractDTO ret = contractsRepository.update(id, number, clientId);
            if (ret == null)
                throw new NotFoundException("Контракт");
            return ContractMapper.INSTANCE.toSoap(ret);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException("Клиент");
        }
        return null;
    }

    public void delete(long id) {
        if (contractsRepository.deleteById(id) == 0) {
            throw new NotFoundException("Контракт");
        }
    }

}
