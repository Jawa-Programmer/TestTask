package ru.jawaprog.test_task.web.soap.services.mappers;

import ru.jawaprog.test_task_mts.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ClientDTO;

import java.util.Collection;

@Mapper
public interface SoapClientMapper {

    SoapClientMapper INSTANCE = Mappers.getMapper(SoapClientMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "fullName", target = "fullName")
    Client fromDto(ClientDTO client);

    Collection<Client> fromDto(Collection<ClientDTO> clients);

}