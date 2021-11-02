package ru.jawaprog.test_task.web.soap.services;

import io.spring.guides.gs_producing_web_service.Client;
import io.spring.guides.gs_producing_web_service.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapClientMapper;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class ClientsSoapService {

    private final ClientsRepository clientsRepository;

    @Autowired
    public ClientsSoapService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
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

    // пакетный модификатор позволит вызывать его только из других сервисов
    ClientDTO getDAO(long id) {
        return clientsRepository.findById(id).orElse(null);
    }

    public Client saveNew(Client client) {
        ClientDTO cl = new ClientDTO();
        cl.setFullName(client.getFullName());
        cl.setType(ClientDTO.ClientType.values()[client.getType().ordinal()]);
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

}
