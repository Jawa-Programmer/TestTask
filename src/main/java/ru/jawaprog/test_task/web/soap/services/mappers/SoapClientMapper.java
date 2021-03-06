package ru.jawaprog.test_task.web.soap.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ClientDTO;
import ru.jawaprog.test_task_mts.Client;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface SoapClientMapper {

    SoapClientMapper INSTANCE = Mappers.getMapper(SoapClientMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "type", target = "type")
    @Mapping(target = "contract", expression = "java(SoapContractMapper.INSTANCE.fromDto(client.getContracts()))")
    Client fromDto(ClientDTO client);

    Collection<Client> fromDto(Collection<ClientDTO> clients);

    List<Client> fromDto(Set<ClientDTO> clients);

}