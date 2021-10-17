package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.web.entities.Contract;

import java.util.Collection;

@Mapper
public interface ContractMapper {
    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "client", target = "client")
    Contract toDto(ContractDTO client);

    Collection<Contract> toDto(Collection<ContractDTO> clients);
}
