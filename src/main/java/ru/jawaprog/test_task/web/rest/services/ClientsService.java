package ru.jawaprog.test_task.web.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.rest.services.mappers.ClientMapper;
import ru.jawaprog.test_task.web.rest.services.mappers.ContractMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    @Autowired
    private PhoneNumbersService phoneNumbersService;

    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public Collection<Client> findAll() {
        return ClientMapper.INSTANCE.toDto(clientsRepository.findAll());
    }


    public Client get(long id) {
        try {
            return ClientMapper.INSTANCE.toDto(clientsRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Client.class);
        }
    }

    // пакетный модификатор позволит вызывать его только из других сервисов
    ClientDTO getDAO(long id) {
        return clientsRepository.findById(id).orElse(null);
    }

    public Client saveNew(Client client) {
        ClientDTO cl = new ClientDTO();
        cl.setFullName(client.getFullName());
        cl.setType(ClientDTO.ClientType.values()[client.getType().ordinal()]);
        return ClientMapper.INSTANCE.toDto(clientsRepository.save(cl));
    }

    public Client update(long id, String fullName, Client.ClientType type) {
        try {
            ClientDTO cl = clientsRepository.findById(id).get();
            if (fullName != null)
                cl.setFullName(fullName);
            if (type != null)
                cl.setType(ClientDTO.ClientType.values()[type.ordinal()]);
            return ClientMapper.INSTANCE.toDto(clientsRepository.save(cl));
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Client.class);
        }
    }

    public Collection<Contract> getClientsContracts(long id) {
        try {
            ClientDTO cl = clientsRepository.findById(id).get();
            return ContractMapper.INSTANCE.toDto(cl.getContracts());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Client.class);
        }
    }

    public void delete(long id) {
        try {
            clientsRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(Client.class);
        }
    }

    public Collection<Client> findByName(String name) {
        return ClientMapper.INSTANCE.toDto(clientsRepository.findAllByName(name));
    }

    public Collection<Client> findByPhoneNumber(String number) {
        Collection<PhoneNumber> phones = phoneNumbersService.getByNumber(number);
        Collection<Client> ret = new HashSet<>();
        for (PhoneNumber phone : phones)
            ret.add(phone.getAccount().getContract().getClient());
        return ret;
    }
}
