package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.repositories.dao.PhoneNumbersDatabaseMapper;
import ru.jawaprog.test_task.repositories.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;

import java.util.Collection;
import java.util.List;

@Service
public class PhoneNumbersService {
    final private PhoneNumbersDatabaseMapper phoneNumbersDatabaseMapper;

    @Autowired
    public PhoneNumbersService(PhoneNumbersDatabaseMapper phoneNumbersDatabaseMapper) {
        this.phoneNumbersDatabaseMapper = phoneNumbersDatabaseMapper;
    }

    public Collection<PhoneNumber> findAllRest() {
        List<PhoneNumberDTO> ret = phoneNumbersDatabaseMapper.findAll();
        return PhoneNumberMapper.INSTANCE.toRest(ret);
    }

    public Collection<PhoneNumber> getByNumber(PhoneNumber number) {
        return PhoneNumberMapper.INSTANCE.toRest(phoneNumbersDatabaseMapper.findAllByNumber(number.getNumber()));
    }

    public PhoneNumber get(PhoneNumber number) {
        PhoneNumberDTO phn = phoneNumbersDatabaseMapper.findById(number.getId());
        if (phn == null) throw new NotFoundException("Номер телефона");
        return PhoneNumberMapper.INSTANCE.toRest(phn);
    }

    public PhoneNumber saveNew(PhoneNumber num) {
        return PhoneNumberMapper.INSTANCE.toRest(phoneNumbersDatabaseMapper.insert(PhoneNumberMapper.INSTANCE.toDto(num)));
    }

    public PhoneNumber update(PhoneNumber number) {
        PhoneNumberDTO phn = phoneNumbersDatabaseMapper.update(PhoneNumberMapper.INSTANCE.toDto(number));
        if (phn == null) throw new NotFoundException("Номер телефона");
        return PhoneNumberMapper.INSTANCE.toRest(phn);

    }

    public void delete(long id) {
        if (phoneNumbersDatabaseMapper.deleteById(id) == 0) throw new NotFoundException("Номер телефона");
    }


    public Collection<ru.jawaprog.test_task_mts.PhoneNumber> findAllSoap() {
        List<PhoneNumberDTO> ret = phoneNumbersDatabaseMapper.findAll();
        return PhoneNumberMapper.INSTANCE.toSoap(ret);
    }

    public Collection<ru.jawaprog.test_task_mts.PhoneNumber> getByNumber(ru.jawaprog.test_task_mts.PhoneNumber number) {
        return PhoneNumberMapper.INSTANCE.toSoap(phoneNumbersDatabaseMapper.findAllByNumber(number.getNumber()));
    }

    public ru.jawaprog.test_task_mts.PhoneNumber get(ru.jawaprog.test_task_mts.PhoneNumber number) {
        PhoneNumberDTO ret = phoneNumbersDatabaseMapper.findById(number.getId());
        if (ret == null)
            throw new NotFoundException("Номер телефона");

        return PhoneNumberMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.PhoneNumber saveNew(ru.jawaprog.test_task_mts.PhoneNumber number) {
        return PhoneNumberMapper.INSTANCE.toSoap(phoneNumbersDatabaseMapper.insert(PhoneNumberMapper.INSTANCE.toDto(number)));

    }

    public ru.jawaprog.test_task_mts.PhoneNumber update(ru.jawaprog.test_task_mts.PhoneNumber number) {
        PhoneNumberDTO ret = phoneNumbersDatabaseMapper.update(PhoneNumberMapper.INSTANCE.toDto(number));
        if (ret == null)
            throw new NotFoundException("Номер телефона");
        return PhoneNumberMapper.INSTANCE.toSoap(ret);
    }

}
