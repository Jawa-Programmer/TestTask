package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.repositories.AccountsRepository;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;

import java.util.Collection;
import java.util.List;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "contract", expression = "java(ContractMapper.INSTANCE.restFromId(account.getContractId()))")
    Account toRest(AccountDTO account);

    default Account restFromId(long id) {
        return toRest(ApplicationContextProvider.getApplicationContext().getBean(AccountsRepository.class).findById(id));
    }

    Collection<Account> toRest(Collection<AccountDTO> accounts);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "phoneNumber", expression = "java(PhoneNumberMapper.INSTANCE.fromAccountId(account.getId()))")
    @Mapping(source = "contractId", target = "contractId")
    ru.jawaprog.test_task_mts.Account toSoap(AccountDTO account);

    default List<ru.jawaprog.test_task_mts.Account> fromContractId(long id) {
        return toSoap(ApplicationContextProvider.getApplicationContext().getBean(AccountsRepository.class).findAccountsByContractId(id));
    }

    Collection<ru.jawaprog.test_task_mts.Account> toSoap(Collection<AccountDTO> accounts);

    List<ru.jawaprog.test_task_mts.Account> toSoap(List<AccountDTO> accounts);

    AccountDTO toDto(Account account);

    AccountDTO toDto(ru.jawaprog.test_task_mts.Account account);
}