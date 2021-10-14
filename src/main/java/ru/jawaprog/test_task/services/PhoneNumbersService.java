package ru.jawaprog.test_task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.AccountDAO;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDAO;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;
import ru.jawaprog.test_task.services.mappers.PhoneNumberMapper;
import ru.jawaprog.test_task.web.entities.PhoneNumberDTO;

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

    public Collection<PhoneNumberDTO> findAll() {
        List<PhoneNumberDAO> ret = phoneNumbersRepository.findAll();
        return PhoneNumberMapper.INSTANCE.toDto(ret);
    }

    public PhoneNumberDTO get(long id) {
        return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.findById(id).orElse(null));
    }

    public PhoneNumberDTO saveNew(PhoneNumberDTO num) {
        AccountDAO acc = accountsService.getDao(num.getAccount().getId());
        if (acc == null) return null;
        PhoneNumberDAO toRet = new PhoneNumberDAO();
        toRet.setNumber(num.getNumber());
        toRet.setAccount(acc);
        return PhoneNumberMapper.INSTANCE.toDto(phoneNumbersRepository.save(toRet));
    }

    public PhoneNumberDTO update(long id, String number, Long accountId) throws Exception {
        PhoneNumberDAO toRet = phoneNumbersRepository.findById(id).orElse(null);
        if (toRet == null) return null;
        if (accountId != null) {
            AccountDAO acc = accountsService.getDao(accountId);
            if (acc == null) throw new Exception();
            toRet.setAccount(acc);
        }
        if (number != null) toRet.setNumber(number);
        return PhoneNumberMapper.INSTANCE.toDto(toRet);
    }

    public void delete(long id) {
        phoneNumbersRepository.deleteById(id);
    }
}
