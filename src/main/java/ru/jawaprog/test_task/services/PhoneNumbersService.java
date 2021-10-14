package ru.jawaprog.test_task.services;

import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDAO;
import ru.jawaprog.test_task.dao.repositories.PhoneNumbersRepository;

import java.util.List;

@Service
public class PhoneNumbersService {
    final private PhoneNumbersRepository phoneNumbersRepository;

    public PhoneNumbersService(PhoneNumbersRepository phoneNumbersRepository) {
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public List<PhoneNumberDAO> findAll() {
        return phoneNumbersRepository.findAll();
    }

    public PhoneNumberDAO get(long id) {
        return phoneNumbersRepository.findById(id).orElse(null);
    }

    public PhoneNumberDAO save(PhoneNumberDAO num) {
        return phoneNumbersRepository.save(num);
    }

    public void delete(long id) {
        phoneNumbersRepository.deleteById(id);
    }
}
