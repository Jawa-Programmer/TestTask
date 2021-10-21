package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ContractsService {

    private final ContractsRepository contractsRepository;

    @Autowired
    private ClientsService clientsService;

    public ContractsService(ContractsRepository contractsRepository) {
        this.contractsRepository = contractsRepository;
    }

    public List<Contract> findAll() {
        return (List<Contract>) ContractMapper.INSTANCE.toDto(contractsRepository.findAll());
    }

    public Contract get(long id) {
        try {
            return ContractMapper.INSTANCE.toDto(contractsRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Contract.class);
        }
    }

    ContractDTO getDAO(long id) {
        return contractsRepository.findById(id).orElse(null);
    }

    public Contract saveNew(Contract c) {
        ClientDTO cl = clientsService.getDAO(c.getClient().getId());
        if (cl == null) throw new ForeignKeyException(Client.class);
        ContractDTO ct = new ContractDTO();
        ct.setClient(cl);
        ct.setNumber(c.getNumber());
        return ContractMapper.INSTANCE.toDto(contractsRepository.save(ct));
    }

    public Contract update(long id, Long number, Long clientId) {
        try {
            ContractDTO ctr = contractsRepository.findById(id).get();
            if (number != null) ctr.setNumber(number);
            if (clientId != null) {
                ClientDTO cl = clientsService.getDAO(clientId);
                if (cl == null) throw new ForeignKeyException(Client.class);
                ctr.setClient(cl);
            }
            return ContractMapper.INSTANCE.toDto(contractsRepository.save(ctr));
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Contract.class);
        }
    }

    public void delete(long id) {
        try {
            contractsRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(Contract.class);
        }
    }

    public Collection<Account> getContractsAccounts(long id) {
        try {
            ContractDTO contractDTO = contractsRepository.findById(id).get();
            return AccountMapper.INSTANCE.toDto(contractDTO.getAccounts());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Contract.class);
        }
    }
}
