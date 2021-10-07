package ru.jawaprog.test_task.services;

import org.springframework.stereotype.Service;
import ru.jawaprog.test_task.entities.Account;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.repositories.AccountsRepository;

import java.util.List;

@Service
public class AccountsService {
    private final AccountsRepository accountsRepository;

    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public List<Account> findAll() {
        return accountsRepository.findAll();
    }

    public Account get(long id) {
        return accountsRepository.findById(id).orElse(null);
    }

    public Account save(Account acc) {
        return accountsRepository.save(acc);
    }

    public void delete(long id) {
        accountsRepository.deleteById(id);
    }
}
