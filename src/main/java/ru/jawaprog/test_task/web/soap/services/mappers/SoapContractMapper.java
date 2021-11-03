package ru.jawaprog.test_task.web.soap.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task_mts.Contract;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface SoapContractMapper {
    SoapContractMapper INSTANCE = Mappers.getMapper(SoapContractMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "account", expression = "java(SoapAccountMapper.INSTANCE.fromDto(contract.getAccounts()))")
    @Mapping(target = "clientId", expression = "java(contract.getClient().getId())")
    Contract fromDto(ContractDTO contract);

    Collection<Contract> fromDto(Collection<ContractDTO> contracts);

    List<Contract> fromDto(Set<ContractDTO> contracts);
}
