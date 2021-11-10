package ru.jawaprog.test_task.web.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.rest.services.mappers.ClientMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.ContractMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;
    private final ContractsRepository contractsRepository;
    private final AccountsRepository accountsRepository;
    private final PhoneNumbersRepository phoneNumbersRepository;

    @Autowired
    public ClientsService(ClientsRepository clientsRepository, ContractsRepository contractsRepository, AccountsRepository accountsRepository, PhoneNumbersRepository phoneNumbersRepository) {
        this.clientsRepository = clientsRepository;
        this.contractsRepository = contractsRepository;
        this.accountsRepository = accountsRepository;
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public Collection<Client> findAll() {
        return ClientMapper.INSTANCE.fromDto(clientsRepository.findAll());
    }

    public Client get(long id) {
        ClientDTO cl = clientsRepository.findById(id);
        if (cl == null)
            throw new NotFoundException(Client.class);
        else
            return ClientMapper.INSTANCE.fromDto(cl);
    }

    public Client saveNew(Client client) {
        return ClientMapper.INSTANCE.fromDto(clientsRepository.insert(client.getFullName(), client.getType().ordinal()));
    }

    public Client update(long id, String fullName, Client.ClientType type) {
        ClientDTO cl = clientsRepository.update(id, fullName, type == null ? null : type.ordinal());
        if (cl == null)
            throw new NotFoundException(Client.class);
        else
            return ClientMapper.INSTANCE.fromDto(cl);
    }

    public Collection<Contract> getClientsContracts(long id) {
        List<ContractDTO> cl = contractsRepository.findByClientId(id);
        return ContractMapper.INSTANCE.fromDto(cl);
    }

    public void delete(long id) {
        int is_del = clientsRepository.deleteById(id);
        if (is_del == 0) throw new NotFoundException(Client.class);
    }

    public Collection<Client> findByName(String name) {
        return ClientMapper.INSTANCE.fromDto(clientsRepository.findAllByName(name));
    }

    public Collection<Client> findByPhoneNumber(String number) {
        Collection<PhoneNumberDTO> phones = phoneNumbersRepository.findAllByNumber(number);
        Collection<Client> ret = new HashSet<>();
        for (PhoneNumberDTO phone : phones)
            ret.add(ClientMapper.INSTANCE.fromDto(
                    clientsRepository.findById(
                            contractsRepository.findById(
                                    accountsRepository.findById(
                                            phone.getAccountId()
                                    ).getContractId()
                            ).getClientId()
                    )));
        return ret;
    }
}
