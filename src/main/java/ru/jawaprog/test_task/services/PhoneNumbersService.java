package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDTO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import java.util.Collection;
import java.util.List;

@Service
public class PhoneNumbersService {
    final private PhoneNumbersRepository phoneNumbersRepository;
    @Autowired
    private AccountsService accountsService;

    public PhoneNumbersService(PhoneNumbersRepository phoneNumbersRepository) {
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public Collection<PhoneNumber> findAll() {
        List<PhoneNumberDTO> ret = phoneNumbersRepository.findAll();
        return PhoneNumberMapper.INSTANCE.toDto(ret);
    }

    public Collection<PhoneNumber> getByNumber(String number) {
        return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.findAllByNumber(number));
    }

    public PhoneNumber get(long id) {
        return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.findById(id).get());
    }

    public PhoneNumber saveNew(PhoneNumber num) {
        AccountDTO acc = accountsService.getDao(num.getAccount().getId());
        if (acc == null) throw new IllegalArgumentException();
        PhoneNumberDTO toRet = new PhoneNumberDTO();
        toRet.setNumber(num.getNumber());
        toRet.setAccount(acc);
        return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.save(toRet));
    }

    public PhoneNumber update(long id, String number, Long accountId) {
        PhoneNumberDTO toRet = phoneNumbersRepository.findById(id).get();
        if (toRet == null) return null;
        if (accountId != null) {
            AccountDTO acc = accountsService.getDao(accountId);
            if (acc == null) throw new IllegalArgumentException();
            toRet.setAccount(acc);
        }
        if (number != null) toRet.setNumber(number);
        return PhoneNumberMapper.INSTANCE.toDto(toRet);
    }

    public void delete(long id) {
        phoneNumbersRepository.deleteById(id);
    }
}
