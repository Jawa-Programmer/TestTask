package ru.jawaprog.test_task.web.soap.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.web.utils.ApplicationContextProvider;
import ru.jawaprog.test_task_mts.PhoneNumber;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SoapPhoneNumberMapper {
    SoapPhoneNumberMapper INSTANCE = Mappers.getMapper(SoapPhoneNumberMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "accountId", source = "accountId")
    PhoneNumber fromDto(PhoneNumberDTO phoneNumber);

    Collection<PhoneNumber> fromDto(Collection<PhoneNumberDTO> phoneNumbers);

    default List<ru.jawaprog.test_task_mts.PhoneNumber> fromAccountId(long id) {
        return fromDto(ApplicationContextProvider.getApplicationContext().getBean(PhoneNumbersRepository.class).findByAccountId(id));
    }

    List<PhoneNumber> fromDto(List<PhoneNumberDTO> phoneNumbers);
}
