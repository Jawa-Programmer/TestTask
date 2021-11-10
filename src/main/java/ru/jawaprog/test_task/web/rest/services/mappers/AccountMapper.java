package ru.jawaprog.test_task.web.rest.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;

import java.util.Collection;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "contract", expression = "java(ContractMapper.INSTANCE.fromId(account.getContractId()))")
    Account fromDto(AccountDTO account);

    default Account fromId(long id) {
        return fromDto(ApplicationContextProvider.getApplicationContext().getBean(AccountsRepository.class).findById(id));
    }

    Collection<Account> fromDto(Collection<AccountDTO> accounts);
}