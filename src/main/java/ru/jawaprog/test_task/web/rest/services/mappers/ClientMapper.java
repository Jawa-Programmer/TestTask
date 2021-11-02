package ru.jawaprog.test_task.web.rest.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task.web.rest.entities.Client;

import java.util.Collection;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "fullName", target = "fullName")
    Client toDto(ClientDTO client);

    Collection<Client> toDto(Collection<ClientDTO> clients);

}
