package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.web.entities.Account;

import java.util.Collection;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "contract", target = "contract")
    Account toDto(AccountDTO account);

    Collection<Account> toDto(Collection<AccountDTO> accounts);
}
