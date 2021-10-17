package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.services.mappers.AccountMapper;
import ru.jawaprog.test_task.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Contract;

import java.util.Collection;
import java.util.List;

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
        return ContractMapper.INSTANCE.toDto(contractsRepository.findById(id).get());
    }

    ContractDTO getDAO(long id) {
        return contractsRepository.findById(id).orElse(null);
    }

    public Contract saveNew(Contract c) {
        ClientDTO cl = clientsService.getDAO(c.getClient().getId());
        if (cl == null) throw new IllegalArgumentException();
        ContractDTO ct = new ContractDTO();
        ct.setClient(cl);
        ct.setNumber(c.getNumber());
        return ContractMapper.INSTANCE.toDto(contractsRepository.save(ct));
    }

    public Contract update(long id, Long number, Long clientId) {
        ContractDTO ctr = contractsRepository.findById(id).get();
        if (number != null) ctr.setNumber(number);
        if (clientId != null) {
            ClientDTO cl = clientsService.getDAO(clientId);
            if (cl == null) throw new IllegalArgumentException();
            ctr.setClient(cl);
        }
        return ContractMapper.INSTANCE.toDto(contractsRepository.save(ctr));
    }

    public void delete(long id) {
        contractsRepository.deleteById(id);
    }

    public Collection<Account> getContractsAccounts(long id) {
        ContractDTO contractDTO = contractsRepository.findById(id).get();
        return AccountMapper.INSTANCE.toDto(contractDTO.getAccounts());
    }
}
