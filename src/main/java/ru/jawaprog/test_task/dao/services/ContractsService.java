package ru.jawaprog.test_task.dao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDAO;
import ru.jawaprog.test_task.dao.entities.ContractDAO;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.mappers.ContractMapper;
import ru.jawaprog.test_task.web.entities.ClientDTO;
import ru.jawaprog.test_task.web.entities.ContractDTO;

import java.util.List;

@Service
public class ContractsService {

    private final ContractsRepository contractsRepository;

    @Autowired
    private ClientsService clientsService;

    public ContractsService(ContractsRepository contractsRepository) {
        this.contractsRepository = contractsRepository;
    }

    public List<ContractDTO> findAll() {
        return (List<ContractDTO>) ContractMapper.INSTANCE.toDto(contractsRepository.findAll());
    }

    public ContractDTO get(long id) {
        return ContractMapper.INSTANCE.toDto(contractsRepository.findById(id).orElse(null));
    }

    public ContractDTO saveNew(ContractDTO c) throws Exception {
        ClientDAO cl = clientsService.getDAO(c.getClient().getId());
        if(cl == null) throw new Exception("client");
        ContractDAO ct = new ContractDAO();
        ct.setClient(cl);
        ct.setNumber(c.getNumber());
        return ContractMapper.INSTANCE.toDto(contractsRepository.save(ct));
    }

    public ContractDTO update(long id, Long number, Long clientId) throws Exception {
        ContractDAO ctr = contractsRepository.findById(id).orElse(null);
        if (number != null) ctr.setNumber(number);
        if (clientId != null) {
            ClientDAO cl = clientsService.getDAO(clientId);
            if (cl == null) throw new Exception("client");
            ctr.setClient(cl);
        }
        return ContractMapper.INSTANCE.toDto(contractsRepository.save(ctr));
    }

    public void delete(long id) {
        contractsRepository.deleteById(id);
    }
}
