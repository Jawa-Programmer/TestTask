package ru.jawaprog.test_task.web.soap.services;


import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task_mts.Client;
import ru.jawaprog.test_task_mts.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapClientMapper;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Service
public class ClientsSoapService {

    private final ClientsRepository clientsRepository;
    private final PhoneNumbersRepository phoneNumbersRepository;

    @Autowired
    public ClientsSoapService(ClientsRepository clientsRepository, PhoneNumbersRepository phoneNumbersRepository) {
        this.clientsRepository = clientsRepository;
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public Collection<Client> findAll() {
        return SoapClientMapper.INSTANCE.fromDto(clientsRepository.findAll());
    }


    public Client get(long id) {
        try {
            return SoapClientMapper.INSTANCE.fromDto(clientsRepository.findById(id).get());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(Client.class);
        }
    }


    public Client saveNew(@NotNull String fullName, @NotNull ClientType type) {
        ClientDTO cl = new ClientDTO();
        cl.setFullName(fullName);
        cl.setType(ClientDTO.ClientType.valueOf(type.value()));
        return SoapClientMapper.INSTANCE.fromDto(clientsRepository.save(cl));
    }

    public Client update(long id, String fullName, ClientType type) {
        try {
            ClientDTO cl = clientsRepository.findById(id).get();
            if (fullName != null)
                cl.setFullName(fullName);
            if (type != null)
                cl.setType(ClientDTO.ClientType.values()[type.ordinal()]);
            return SoapClientMapper.INSTANCE.fromDto(clientsRepository.save(cl));
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
        return SoapClientMapper.INSTANCE.fromDto(clientsRepository.findAllByName(name));
    }

    public Collection<Client> findByPhoneNumber(String number) {
        Collection<PhoneNumberDTO> phones = phoneNumbersRepository.findAllByNumber(number);
        Collection<Client> ret = new HashSet<>();
        for (PhoneNumberDTO phone : phones)
            ret.add(SoapClientMapper.INSTANCE.fromDto(phone.getAccount().getContract().getClient()));
        return ret;
    }
}
