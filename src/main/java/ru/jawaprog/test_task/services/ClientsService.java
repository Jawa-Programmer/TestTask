package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.services.mappers.ClientMapper;
import ru.jawaprog.test_task.services.mappers.ContractMapper;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
        return ClientMapper.INSTANCE.toDto(clientsRepository.findById(id).orElse(null));
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
        ClientDTO cl = clientsRepository.findById(id).orElse(null);
        if (cl == null) return null;
        if (fullName != null)
            cl.setFullName(fullName);
        if (type != null)
            cl.setType(ClientDTO.ClientType.values()[type.ordinal()]);
        return ClientMapper.INSTANCE.toDto(clientsRepository.save(cl));
    }

    public Collection<Contract> getClientsContracts(long id) throws Exception {
        ClientDTO cl = clientsRepository.findById(id).orElse(null);
        if (cl == null) throw new Exception();
        return ContractMapper.INSTANCE.toDto(cl.getContracts());
    }

    public void delete(long id) {
        clientsRepository.deleteById(id);
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
