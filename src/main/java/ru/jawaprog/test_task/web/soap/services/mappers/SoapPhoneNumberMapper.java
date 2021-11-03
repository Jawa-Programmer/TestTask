package ru.jawaprog.test_task.web.soap.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task_mts.PhoneNumber;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface SoapPhoneNumberMapper {
    SoapPhoneNumberMapper INSTANCE = Mappers.getMapper(SoapPhoneNumberMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "accountId", expression = "java(phoneNumber.getAccount().getId())")
    PhoneNumber fromDto(PhoneNumberDTO phoneNumber);

    Collection<PhoneNumber> fromDto(Collection<PhoneNumberDTO> phoneNumbers);

    List<PhoneNumber> fromDto(Set<PhoneNumberDTO> phoneNumbers);
}
