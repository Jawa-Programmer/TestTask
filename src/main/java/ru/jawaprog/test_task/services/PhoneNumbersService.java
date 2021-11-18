package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;

import java.util.Collection;
import java.util.List;

@Service
public class PhoneNumbersService {
    final private PhoneNumbersRepository phoneNumbersRepository;

    @Autowired
    public PhoneNumbersService(PhoneNumbersRepository phoneNumbersRepository) {
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public Collection<PhoneNumber> findAllRest() {
        List<PhoneNumberDTO> ret = phoneNumbersRepository.findAll();
        return PhoneNumberMapper.INSTANCE.toRest(ret);
    }

    public Collection<PhoneNumber> getByNumber(PhoneNumber number) {
        return PhoneNumberMapper.INSTANCE.toRest(phoneNumbersRepository.findAllByNumber(number.getNumber()));
    }

    public PhoneNumber get(PhoneNumber number) {
        PhoneNumberDTO phn = phoneNumbersRepository.findById(number.getId());
        if (phn == null) throw new NotFoundException("Номер телефона");
        return PhoneNumberMapper.INSTANCE.toRest(phn);
    }

    public PhoneNumber saveNew(PhoneNumber num) {
        try {
            return PhoneNumberMapper.INSTANCE.toRest(phoneNumbersRepository.insert(PhoneNumberMapper.INSTANCE.toDto(num)));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Счёт");
        }
        return null;
    }

    public PhoneNumber update(PhoneNumber number) {
        try {
            PhoneNumberDTO phn = phoneNumbersRepository.update(PhoneNumberMapper.INSTANCE.toDto(number));
            if (phn == null) throw new NotFoundException("Номер телефона");
            return PhoneNumberMapper.INSTANCE.toRest(phn);
        } catch (
                DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Счёт");
        }
        return null;
    }

    public void delete(long id) {
        if (phoneNumbersRepository.deleteById(id) == 0) throw new NotFoundException("Номер телефона");
    }


    public Collection<ru.jawaprog.test_task_mts.PhoneNumber> findAllSoap() {
        List<PhoneNumberDTO> ret = phoneNumbersRepository.findAll();
        return PhoneNumberMapper.INSTANCE.toSoap(ret);
    }

    public Collection<ru.jawaprog.test_task_mts.PhoneNumber> getByNumber(ru.jawaprog.test_task_mts.PhoneNumber number) {
        return PhoneNumberMapper.INSTANCE.toSoap(phoneNumbersRepository.findAllByNumber(number.getNumber()));
    }

    public ru.jawaprog.test_task_mts.PhoneNumber get(ru.jawaprog.test_task_mts.PhoneNumber number) {
        PhoneNumberDTO ret = phoneNumbersRepository.findById(number.getId());
        if (ret == null)
            throw new NotFoundException("Номер телефона");

        return PhoneNumberMapper.INSTANCE.toSoap(ret);
    }

    public ru.jawaprog.test_task_mts.PhoneNumber saveNew(ru.jawaprog.test_task_mts.PhoneNumber number) {
        try {
            return PhoneNumberMapper.INSTANCE.toSoap(phoneNumbersRepository.insert(PhoneNumberMapper.INSTANCE.toDto(number)));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Счёт");
        }
        return null;
    }

    public ru.jawaprog.test_task_mts.PhoneNumber update(ru.jawaprog.test_task_mts.PhoneNumber number) {
        try {
            PhoneNumberDTO ret = phoneNumbersRepository.update(PhoneNumberMapper.INSTANCE.toDto(number));
            if (ret == null)
                throw new NotFoundException("Номер телефона");
            return PhoneNumberMapper.INSTANCE.toSoap(ret);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа"))
                throw new ForeignKeyException("Счёт");
        }
        return null;
    }

}
