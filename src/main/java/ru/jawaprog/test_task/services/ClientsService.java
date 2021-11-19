package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsDatabaseMapper;
import ru.jawaprog.test_task.dao.repositories.ClientsDatabaseMapper;
import ru.jawaprog.test_task.dao.repositories.ContractsDatabaseMapper;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersDatabaseMapper;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.services.mappers.ClientMapper;
import ru.jawaprog.test_task.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class ClientsService {

    private final ClientsDatabaseMapper clientsDatabaseMapper;
    private final ContractsDatabaseMapper contractsDatabaseMapper;
    private final AccountsDatabaseMapper accountsDatabaseMapper;
    private final PhoneNumbersDatabaseMapper phoneNumbersDatabaseMapper;

    @Autowired
    public ClientsService(ClientsDatabaseMapper clientsDatabaseMapper, ContractsDatabaseMapper contractsDatabaseMapper, AccountsDatabaseMapper accountsDatabaseMapper, PhoneNumbersDatabaseMapper phoneNumbersDatabaseMapper) {
        this.clientsDatabaseMapper = clientsDatabaseMapper;
        this.contractsDatabaseMapper = contractsDatabaseMapper;
        this.accountsDatabaseMapper = accountsDatabaseMapper;
        this.phoneNumbersDatabaseMapper = phoneNumbersDatabaseMapper;
    }

    public Collection<Client> findAllRest() {
        return ClientMapper.INSTANCE.toRest(clientsDatabaseMapper.findAll());
    }

    public Client get(Client client) {
        ClientDTO cl = clientsDatabaseMapper.findById(client.getId());
        if (cl == null)
            throw new NotFoundException("Клиент");
        else
            return ClientMapper.INSTANCE.toRest(cl);
    }

    public Client saveNew(Client client) {
        return ClientMapper.INSTANCE.toRest(clientsDatabaseMapper.insert(client.getFullName(), client.getType().ordinal()));
    }

    public Client update(Client client) {
        ClientDTO cl = clientsDatabaseMapper.update(ClientMapper.INSTANCE.toDto(client));
        if (cl == null)
            throw new NotFoundException("Клиент");
        else
            return ClientMapper.INSTANCE.toRest(cl);
    }

    public Collection<Contract> getClientsContracts(Client client) {
        if (!clientsDatabaseMapper.exists(client.getId()))
            throw new NotFoundException("Клиент");
        List<ContractDTO> cl = contractsDatabaseMapper.findByClientId(client.getId());
        return ContractMapper.INSTANCE.toRest(cl);
    }

    public void delete(long id) {
        if (clientsDatabaseMapper.deleteById(id) == 0) {
            throw new NotFoundException("Клиент");
        }
    }

    public Collection<Client> findByName(Client client) {
        return ClientMapper.INSTANCE.toRest(clientsDatabaseMapper.findAllByName(client.getFullName()));
    }

    public Collection<Client> findByPhoneNumber(PhoneNumber phoneNumber) {
        Collection<PhoneNumberDTO> phones = phoneNumbersDatabaseMapper.findAllByNumber(phoneNumber.getNumber());
        Collection<Client> ret = new HashSet<>();
        for (PhoneNumberDTO phone : phones)
            ret.add(ClientMapper.INSTANCE.toRest(
                    clientsDatabaseMapper.findById(
                            contractsDatabaseMapper.findById(
                                    accountsDatabaseMapper.findById(
                                            phone.getAccountId()
                                    ).getContractId()
                            ).getClientId()
                    )));
        return ret;
    }


    public List<ru.jawaprog.test_task_mts.Client> findAllSoap() {
        return ClientMapper.INSTANCE.toSoap(clientsDatabaseMapper.findAll());
    }


    public ru.jawaprog.test_task_mts.Client get(long id) {
        ClientDTO ret = clientsDatabaseMapper.findById(id);
        if (ret == null)
            throw new NotFoundException("Клиент");
        return ClientMapper.INSTANCE.toSoap(ret);
    }


    public ru.jawaprog.test_task_mts.Client saveNew(ru.jawaprog.test_task_mts.Client client) {
        ClientDTO ret = clientsDatabaseMapper.insert(client.getFullName(), client.getType().ordinal());
        return ClientMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.Client update(ru.jawaprog.test_task_mts.Client client) {
        ClientDTO ret = clientsDatabaseMapper.update(ClientMapper.INSTANCE.toDto(client));
        if (ret == null)
            throw new NotFoundException("Клиент");
        return ClientMapper.INSTANCE.toSoap(ret);
    }

    public Collection<ru.jawaprog.test_task_mts.Client> findByName(ru.jawaprog.test_task_mts.Client client) {
        return ClientMapper.INSTANCE.toSoap(clientsDatabaseMapper.findAllByName(client.getFullName()));
    }

    public Collection<ru.jawaprog.test_task_mts.Client> findByPhoneNumber(ru.jawaprog.test_task_mts.PhoneNumber phoneNumber) {
        Collection<PhoneNumberDTO> phones = phoneNumbersDatabaseMapper.findAllByNumber(phoneNumber.getNumber());
        Collection<ru.jawaprog.test_task_mts.Client> ret = new HashSet<>();
        for (PhoneNumberDTO phone : phones)
            ret.add(ClientMapper.INSTANCE.toSoap(
                    clientsDatabaseMapper.findById(
                            contractsDatabaseMapper.findById(
                                    accountsDatabaseMapper.findById(phone.getAccountId()).getContractId()).getClientId()
                    )));
        return ret;
    }
}
