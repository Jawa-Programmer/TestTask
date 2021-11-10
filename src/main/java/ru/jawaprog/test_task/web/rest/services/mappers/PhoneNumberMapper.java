package ru.jawaprog.test_task.web.rest.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;

import java.util.Collection;

@Mapper
public interface PhoneNumberMapper {
    PhoneNumberMapper INSTANCE = Mappers.getMapper(PhoneNumberMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(target = "account", expression = "java(AccountMapper.INSTANCE.fromId(phone.getAccountId()))")
    PhoneNumber fromDto(PhoneNumberDTO phone);

    Collection<PhoneNumber> fromDto(Collection<PhoneNumberDTO> phones);
}
