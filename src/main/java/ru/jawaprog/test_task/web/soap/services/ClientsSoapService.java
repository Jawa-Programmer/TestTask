package ru.jawaprog.test_task.web.soap.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.soap.services.mappers.SoapClientMapper;
import ru.jawaprog.test_task_mts.Client;
import ru.jawaprog.test_task_mts.ClientType;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Service
public class ClientsSoapService {

    private final ClientsRepository clientsRepository;
    private final ContractsRepository contractsRepository;
    private final AccountsRepository accountsRepository;
    private final PhoneNumbersRepository phoneNumbersRepository;

    @Autowired
    public ClientsSoapService(ClientsRepository clientsRepository, ContractsRepository contractsRepository, AccountsRepository accountsRepository, PhoneNumbersRepository phoneNumbersRepository) {
        this.clientsRepository = clientsRepository;
        this.contractsRepository = contractsRepository;
        this.accountsRepository = accountsRepository;
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public Collection<Client> findAll() {
        return SoapClientMapper.INSTANCE.fromDto(clientsRepository.findAll());
    }


    public Client get(long id) {
        ClientDTO ret = clientsRepository.findById(id);
        if (ret == null)
            throw new NotFoundException("Клиент");
        return SoapClientMapper.INSTANCE.fromDto(ret);
    }


    public Client saveNew(@NotNull String fullName, @NotNull ClientType type) {
        ClientDTO ret = clientsRepository.insert(fullName, type.ordinal());
        return SoapClientMapper.INSTANCE.fromDto(ret);
    }

    public Client update(long id, String fullName, ClientType type) {
        ClientDTO ret = clientsRepository.update(id, fullName, type == null ? null : type.ordinal());
        if (ret == null)
            throw new NotFoundException("Клиент");
        return SoapClientMapper.INSTANCE.fromDto(ret);
    }

    public void delete(long id) {
        if (clientsRepository.deleteById(id) == 0) {
            throw new NotFoundException("Клиент");
        }
    }

    public Collection<Client> findByName(String name) {
        return SoapClientMapper.INSTANCE.fromDto(clientsRepository.findAllByName(name));
    }

    public Collection<Client> findByPhoneNumber(String number) {
        Collection<PhoneNumberDTO> phones = phoneNumbersRepository.findAllByNumber(number);
        Collection<Client> ret = new HashSet<>();
        for (PhoneNumberDTO phone : phones)
            ret.add(SoapClientMapper.INSTANCE.fromDto(
                    clientsRepository.findById(
                            contractsRepository.findById(
                                    accountsRepository.findById(phone.getAccountId()).getContractId()).getClientId()
                    )));
        return ret;
    }
}
