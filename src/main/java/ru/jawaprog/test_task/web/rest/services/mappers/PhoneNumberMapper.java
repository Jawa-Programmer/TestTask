package ru.jawaprog.test_task.web.rest.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PhoneNumberMapper {
    PhoneNumberMapper INSTANCE = Mappers.getMapper(PhoneNumberMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "account", expression = "java(AccountMapper.INSTANCE.restFromId(phone.getAccountId()))")
    PhoneNumber toRest(PhoneNumberDTO phone);

    Collection<PhoneNumber> toRest(Collection<PhoneNumberDTO> phones);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "accountId", source = "accountId")
    ru.jawaprog.test_task_mts.PhoneNumber toSoap(PhoneNumberDTO phoneNumber);

    Collection<ru.jawaprog.test_task_mts.PhoneNumber> toSoap(Collection<PhoneNumberDTO> phoneNumbers);

    default List<ru.jawaprog.test_task_mts.PhoneNumber> fromAccountId(long id) {
        return toSoap(ApplicationContextProvider.getApplicationContext().getBean(PhoneNumbersRepository.class).findByAccountId(id));
    }

    List<ru.jawaprog.test_task_mts.PhoneNumber> toSoap(List<PhoneNumberDTO> phoneNumbers);
}
