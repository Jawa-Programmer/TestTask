package ru.jawaprog.test_task.web.soap.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;
import ru.jawaprog.test_task_mts.Account;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SoapAccountMapper {
    SoapAccountMapper INSTANCE = Mappers.getMapper(SoapAccountMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "phoneNumber", expression = "java(SoapPhoneNumberMapper.INSTANCE.fromAccountId(account.getId()))")
    @Mapping(source = "contractId", target = "contractId")
    Account fromDto(AccountDTO account);

    default List<ru.jawaprog.test_task_mts.Account> fromContractId(long id) {
        return fromDto(ApplicationContextProvider.getApplicationContext().getBean(AccountsRepository.class).findAccountsByContractId(id));
    }

    Collection<Account> fromDto(Collection<AccountDTO> accounts);

    List<Account> fromDto(List<AccountDTO> accounts);
}
