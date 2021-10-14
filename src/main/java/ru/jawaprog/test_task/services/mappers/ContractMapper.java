package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ContractDAO;
import ru.jawaprog.test_task.web.entities.ContractDTO;

import java.util.Collection;

@Mapper
public interface ContractMapper {
    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "client", target = "client")
    })
    ContractDTO toDto(ContractDAO client);

    Collection<ContractDTO > toDto(Collection<ContractDAO > clients);
}
