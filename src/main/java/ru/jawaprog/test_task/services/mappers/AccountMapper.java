package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.AccountDAO;
import ru.jawaprog.test_task.web.entities.AccountDTO;

import java.util.Collection;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "contract", target = "contract")
    })
    AccountDTO toDto(AccountDAO account);

    Collection<AccountDTO> toDto(Collection<AccountDAO> accounts);
}
