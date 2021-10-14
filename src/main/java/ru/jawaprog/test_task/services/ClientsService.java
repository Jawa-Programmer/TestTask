package ru.jawaprog.test_task.services;

import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDAO;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.services.mappers.ClientMapper;
import ru.jawaprog.test_task.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.entities.ClientDTO;
import ru.jawaprog.test_task.web.entities.ContractDTO;

import java.util.Collection;
import java.util.List;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public Collection<ClientDTO> findAll() {
        return ClientMapper.INSTANCE.toDto(clientsRepository.findAll());
    }


    public ClientDTO get(long id) {
        return ClientMapper.INSTANCE.toDto(clientsRepository.findById(id).orElse(null));
    }

    // пакетный модификатор позволит вызывать его только из других сервисов
    ClientDAO getDAO(long id) {
        return clientsRepository.findById(id).orElse(null);
    }

    public ClientDTO saveNew(ClientDTO client) {
        ClientDAO cl = new ClientDAO();
        cl.setFullName(client.getFullName());
        cl.setType(ClientDAO.ClientType.values()[client.getType().ordinal()]);
        return ClientMapper.INSTANCE.toDto(clientsRepository.save(cl));
    }

    public ClientDTO update(long id, String fullName, ClientDTO.ClientType type) {
        ClientDAO cl = clientsRepository.findById(id).orElse(null);
        if (cl == null) return null;
        if (fullName != null)
            cl.setFullName(fullName);
        if (type != null)
            cl.setType(ClientDAO.ClientType.values()[type.ordinal()]);
        return ClientMapper.INSTANCE.toDto(clientsRepository.save(cl));
    }

    public Collection<ContractDTO> getClientsContracts(long id) throws Exception {
        ClientDAO cl = clientsRepository.findById(id).orElse(null);
        if (cl == null) throw new Exception();
        return ContractMapper.INSTANCE.toDto(cl.getContracts());
    }

    public void delete(long id) {
        clientsRepository.deleteById(id);
    }

    public Collection<ClientDTO> findByName(String name) {
        return ClientMapper.INSTANCE.toDto(clientsRepository.findAllByName(name));
    }
}
