package ru.jawaprog.test_task.web.soap.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.ContractDTO;
import ru.jawaprog.test_task.dao.repositories.ContractsRepository;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;
import ru.jawaprog.test_task_mts.Contract;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SoapContractMapper {
    SoapContractMapper INSTANCE = Mappers.getMapper(SoapContractMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "account", expression = "java(SoapAccountMapper.INSTANCE.fromContractId(contract.getId()))")
    @Mapping(target = "clientId", source = "clientId")
    Contract fromDto(ContractDTO contract);

    Collection<Contract> fromDto(Collection<ContractDTO> contracts);


    default List<ru.jawaprog.test_task_mts.Contract> fromClientId(long id) {
        return fromDto(ApplicationContextProvider.getApplicationContext().getBean(ContractsRepository.class).findByClientId(id));
    }

    List<Contract> fromDto(List<ContractDTO> contracts);
}
