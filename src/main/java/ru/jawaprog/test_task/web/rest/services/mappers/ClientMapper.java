package ru.jawaprog.test_task.web.rest.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.dao.repositories.ClientsRepository;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;

import java.util.Collection;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(target = "type", expression = "java(ru.jawaprog.test_task.web.rest.entities.Client.ClientType.values()[client.getType()])")
    @Mapping(source = "fullName", target = "fullName")
    Client fromDto(ClientDTO client);

    default Client fromId(long id) {
        return fromDto(ApplicationContextProvider.getApplicationContext().getBean(ClientsRepository.class).findById(id));
    }

    Collection<Client> fromDto(Collection<ClientDTO> clients);

}
