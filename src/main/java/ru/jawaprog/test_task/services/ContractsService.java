package ru.jawaprog.test_task.services;

import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.entities.Contract;
import ru.jawaprog.test_task.repositories.ContractsRepository;

import java.util.List;

@Service
public class ContractsService {

    private final ContractsRepository contractsRepository;

    public ContractsService(ContractsRepository contractsRepository) {
        this.contractsRepository = contractsRepository;
    }

    public List<Contract> findAll() {
        return contractsRepository.findAll();
    }

    public Contract get(long id) {
        return contractsRepository.findById(id).orElse(null);
    }

    public Contract save(Contract c) {
        return contractsRepository.save(c);
    }

    public void delete(long id) {
        contractsRepository.deleteById(id);
    }
}
