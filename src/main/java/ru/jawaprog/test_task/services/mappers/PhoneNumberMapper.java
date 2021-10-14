package ru.jawaprog.test_task.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDAO;
import ru.jawaprog.test_task.web.entities.PhoneNumberDTO;

import java.util.Collection;

@Mapper
public interface PhoneNumberMapper {
    PhoneNumberMapper INSTANCE = Mappers.getMapper(PhoneNumberMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "account", target = "account")
    })
    PhoneNumberDTO toDto(PhoneNumberDAO client);

    Collection<PhoneNumberDTO> toDto(Collection<PhoneNumberDAO> clients);
}
