package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ClientDAO;
import ru.jawaprog.test_task.web.entities.ClientDTO;

import java.util.Collection;
import java.util.List;

@Mapper

public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "fullName", target = "fullName")
    })
    ClientDTO toDto(ClientDAO client);

    Collection<ClientDTO> toDto(Collection<ClientDAO> clients);
/*
    @InheritInverseConfiguration
    DAOClient dtoToDaoClient(DTOClient client);*/
}
