package ru.jawaprog.test_task.web.rest.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;

import java.util.List;

@Mapper
public interface ContractMapper {

    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "client", expression = "java(ClientMapper.INSTANCE.fromId(contract.getClientId()))")
    Contract fromDto(ContractDTO contract);

    default Contract fromId(long id) {
        return fromDto(ApplicationContextProvider.getApplicationContext().getBean(ContractsRepository.class).findById(id));
    }

    List<Contract> fromDto(List<ContractDTO> contracts);
}
