package ru.jawaprog.test_task.web.soap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapContractMapper;
import ru.jawaprog.test_task_mts.Client;
import ru.jawaprog.test_task_mts.Contract;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class ContractsSoapService {

    private final ClientsRepository clientsRepository;
    private final ContractsRepository contractsRepository;


    @Autowired
    public ContractsSoapService(ClientsRepository clientsRepository, ContractsRepository contractsRepository) {
        this.clientsRepository = clientsRepository;
        this.contractsRepository = contractsRepository;
    }

    public Collection<Contract> findAll() {
        return SoapContractMapper.INSTANCE.fromDto(contractsRepository.findAll());
    }

    public Contract get(long id) {
        try {
            return SoapContractMapper.INSTANCE.fromDto(contractsRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("Контракт");
        }
    }

    public Contract saveNew(long number, long clientId) {
        ClientDTO cl = clientsRepository.findById(clientId).orElse(null);
        if (cl == null) throw new ForeignKeyException("Клиент");
        ContractDTO ct = new ContractDTO();
        ct.setClient(cl);
        ct.setNumber(number);
        return SoapContractMapper.INSTANCE.fromDto(contractsRepository.save(ct));
    }

    public Contract update(long id, Long number, Long clientId) {
        try {
            ContractDTO ctr = contractsRepository.findById(id).get();
            if (number != null) ctr.setNumber(number);
            if (clientId != null) {
                ClientDTO cl = clientsRepository.findById(clientId).orElse(null);
                if (cl == null) throw new ForeignKeyException("Клиент");
                ctr.setClient(cl);
            }
            return SoapContractMapper.INSTANCE.fromDto(contractsRepository.save(ctr));
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("Контракт");
        }
    }

    public void delete(long id) {
        try {
            contractsRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Контракт");
        }
    }

}
