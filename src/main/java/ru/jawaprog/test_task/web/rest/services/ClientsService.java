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
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.rest.services.mappers.ClientMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;

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

    public Collection<Client> findAllRest() {
        return ClientMapper.INSTANCE.toRest(clientsRepository.findAll());
    }

    public Client get(Client client) {
        ClientDTO cl = clientsRepository.findById(client.getId());
        if (cl == null)
            throw new NotFoundException("Клиент");
        else
            return ClientMapper.INSTANCE.toRest(cl);
    }

    public Client saveNew(Client client) {
        return ClientMapper.INSTANCE.toRest(clientsRepository.insert(client.getFullName(), client.getType().ordinal()));
    }

    public Client update(Client client) {
        ClientDTO cl = clientsRepository.update(ClientMapper.INSTANCE.toDto(client));
        if (cl == null)
            throw new NotFoundException("Клиент");
        else
            return ClientMapper.INSTANCE.toRest(cl);
    }

    public Collection<Contract> getClientsContracts(Client client) {
        List<ContractDTO> cl = contractsRepository.findByClientId(client.getId());
        return ContractMapper.INSTANCE.toRest(cl);
    }

    public void delete(long id) {
        if (clientsRepository.deleteById(id) == 0) {
            throw new NotFoundException("Клиент");
        }
    }

    public Collection<Client> findByName(Client client) {
        return ClientMapper.INSTANCE.toRest(clientsRepository.findAllByName(client.getFullName()));
    }

    public Collection<Client> findByPhoneNumber(PhoneNumber phoneNumber) {
        Collection<PhoneNumberDTO> phones = phoneNumbersRepository.findAllByNumber(phoneNumber.getNumber());
        Collection<Client> ret = new HashSet<>();
        for (PhoneNumberDTO phone : phones)
            ret.add(ClientMapper.INSTANCE.toRest(
                    clientsRepository.findById(
                            contractsRepository.findById(
                                    accountsRepository.findById(
                                            phone.getAccountId()
                                    ).getContractId()
                            ).getClientId()
                    )));
        return ret;
    }


    public Collection<ru.jawaprog.test_task_mts.Client> findAllSoap() {
        return ClientMapper.INSTANCE.toSoap(clientsRepository.findAll());
    }


    public ru.jawaprog.test_task_mts.Client get(long id) {
        ClientDTO ret = clientsRepository.findById(id);
        if (ret == null)
            throw new NotFoundException("Клиент");
        return ClientMapper.INSTANCE.toSoap(ret);
    }


    public ru.jawaprog.test_task_mts.Client saveNew(ru.jawaprog.test_task_mts.Client client) {
        ClientDTO ret = clientsRepository.insert(client.getFullName(), client.getType().ordinal());
        return ClientMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.Client update(ru.jawaprog.test_task_mts.Client client) {
        ClientDTO ret = clientsRepository.update(ClientMapper.INSTANCE.toDto(client));
        if (ret == null)
            throw new NotFoundException("Клиент");
        return ClientMapper.INSTANCE.toSoap(ret);
    }

    public Collection<ru.jawaprog.test_task_mts.Client> findByName(ru.jawaprog.test_task_mts.Client client) {
        return ClientMapper.INSTANCE.toSoap(clientsRepository.findAllByName(client.getFullName()));
    }

    public Collection<ru.jawaprog.test_task_mts.Client> findByPhoneNumber(ru.jawaprog.test_task_mts.PhoneNumber phoneNumber) {
        Collection<PhoneNumberDTO> phones = phoneNumbersRepository.findAllByNumber(phoneNumber.getNumber());
        Collection<ru.jawaprog.test_task_mts.Client> ret = new HashSet<>();
        for (PhoneNumberDTO phone : phones)
            ret.add(ClientMapper.INSTANCE.toSoap(
                    clientsRepository.findById(
                            contractsRepository.findById(
                                    accountsRepository.findById(phone.getAccountId()).getContractId()).getClientId()
                    )));
        return ret;
    }
}
