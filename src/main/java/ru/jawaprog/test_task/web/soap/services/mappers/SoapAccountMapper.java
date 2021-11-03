package ru.jawaprog.test_task.web.soap.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task_mts.Account;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface SoapAccountMapper {
    SoapAccountMapper INSTANCE = Mappers.getMapper(SoapAccountMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "phoneNumber", expression = "java(SoapPhoneNumberMapper.INSTANCE.fromDto(account.getPhoneNumbers()))")
    @Mapping(target = "contractId", expression = "java(account.getContract().getId())")
    Account fromDto(AccountDTO account);

    Collection<Account> fromDto(Collection<AccountDTO> accounts);

    List<Account> fromDto(Set<AccountDTO> accounts);
}
