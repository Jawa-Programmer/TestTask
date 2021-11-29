package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.repositories.dao.ContractsDatabaseMapper;
import ru.jawaprog.test_task.repositories.entities.ContractDTO;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ContractMapper {

    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "client", expression = "java(ClientMapper.INSTANCE.restFromId(contract.getClientId()))")
    Contract toRest(ContractDTO contract);

    default Contract restFromId(long id) {
        return toRest(ApplicationContextProvider.getApplicationContext().getBean(ContractsDatabaseMapper.class).findById(id));
    }

    List<Contract> toRest(List<ContractDTO> contracts);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "account", expression = "java(AccountMapper.INSTANCE.fromContractId(contract.getId()))")
    @Mapping(target = "clientId", source = "clientId")
    ru.jawaprog.test_task_mts.Contract toSoap(ContractDTO contract);

    Collection<ru.jawaprog.test_task_mts.Contract> toSoap(Collection<ContractDTO> contracts);


    default List<ru.jawaprog.test_task_mts.Contract> fromClientId(long id) {
        return toSoap(ApplicationContextProvider.getApplicationContext().getBean(ContractsDatabaseMapper.class).findByClientId(id));
    }

    List<ru.jawaprog.test_task_mts.Contract> toSoap(List<ContractDTO> contracts);


    ContractDTO toDto(ru.jawaprog.test_task_mts.Contract contract);


    @Mapping(target = "clientId", expression = "java(contract.getClient()==null?null:contract.getClient().getId())")
    ContractDTO toDto(Contract contract);
}
