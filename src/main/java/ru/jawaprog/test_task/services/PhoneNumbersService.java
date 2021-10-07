package ru.jawaprog.test_task.services;

import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.entities.Account;
import ru.jawaprog.test_task.entities.PhoneNumber;
import ru.jawaprog.test_task.repositories.PhoneNumbersRepository;

import java.util.List;

@Service
public class PhoneNumbersService {
    final private PhoneNumbersRepository phoneNumbersRepository;

    public PhoneNumbersService(PhoneNumbersRepository phoneNumbersRepository) {
        this.phoneNumbersRepository = phoneNumbersRepository;
    }

    public List<PhoneNumber> findAll() {
        return phoneNumbersRepository.findAll();
    }

    public PhoneNumber getClient(long id) {
        return phoneNumbersRepository.findById(id).orElse(null);
    }

    public void save(PhoneNumber num) {
        phoneNumbersRepository.save(num);
    }

    public void delete(long id) {
        phoneNumbersRepository.deleteById(id);
    }
}
