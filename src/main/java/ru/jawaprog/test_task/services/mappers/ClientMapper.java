package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.repositories.dao.ClientsDatabaseMapper;
import ru.jawaprog.test_task.repositories.entities.ClientDTO;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;
import ru.jawaprog.test_task_mts.ClientType;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);


    @Mapping(target = "type", expression = "java(ru.jawaprog.test_task.web.rest.entities.Client.ClientType.values()[client.getType()])")
    Client toRest(ClientDTO client);

    default Client restFromId(long id) {
        return toRest(ApplicationContextProvider.getApplicationContext().getBean(ClientsDatabaseMapper.class).findById(id));
    }

    Collection<Client> toRest(Collection<ClientDTO> clients);

    @Mapping(target = "type", expression = "java(ru.jawaprog.test_task_mts.ClientType.values()[client.getType()])")
    @Mapping(target = "contract", expression = "java(ContractMapper.INSTANCE.fromClientId(client.getId()))")
    ru.jawaprog.test_task_mts.Client toSoap(ClientDTO client);

    List<ru.jawaprog.test_task_mts.Client> toSoap(Collection<ClientDTO> clients);

    @Mapping(target = "type", expression = "java(typeToDto(client.getType()))")
    ClientDTO toDto(Client client);

    default Integer typeToDto(Client.ClientType type) {
        if (type == null) return null;
        return type.ordinal();
    }

    default Integer typeToDto(ClientType type) {
        if (type == null) return null;
        return type.ordinal();
    }

    @Mapping(target = "type", expression = "java(typeToDto(client.getType()))")
    ClientDTO toDto(ru.jawaprog.test_task_mts.Client client);
}
