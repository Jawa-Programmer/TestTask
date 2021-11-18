package ru.jawaprog.test_task.web.rest.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);


    @Mapping(target = "type", expression = "java(ru.jawaprog.test_task.web.rest.entities.Client.ClientType.values()[client.getType()])")
    Client toRest(ClientDTO client);

    default Client restFromId(long id) {
        return toRest(ApplicationContextProvider.getApplicationContext().getBean(ClientsRepository.class).findById(id));
    }

    Collection<Client> toRest(Collection<ClientDTO> clients);

    @Mapping(target = "type", expression = "java(ru.jawaprog.test_task_mts.ClientType.values()[client.getType()])")
    @Mapping(target = "contract", expression = "java(ContractMapper.INSTANCE.fromClientId(client.getId()))")
    ru.jawaprog.test_task_mts.Client toSoap(ClientDTO client);

    Collection<ru.jawaprog.test_task_mts.Client> toSoap(Collection<ClientDTO> clients);

    List<ru.jawaprog.test_task_mts.Client> toSoap(Set<ClientDTO> clients);

    @Mapping(target = "type", expression = "java(client.getType().ordinal())")
    ClientDTO toDto(Client client);

    @Mapping(target = "type", expression = "java(client.getType().ordinal())")
    ClientDTO toDto(ru.jawaprog.test_task_mts.Client client);
}
