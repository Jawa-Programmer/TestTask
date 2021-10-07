package ru.jawaprog.test_task.services;

import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.repositories.ClientsRepository;

import java.util.List;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public List<Client> findAll() {
        return clientsRepository.findAll();
    }



    public Client get(long id) {
        return clientsRepository.findById(id).orElse(null);
    }

    public Client save(Client client) {
        return clientsRepository.save(client);
    }

    public void delete(long id) {
        clientsRepository.deleteById(id);
    }

}
