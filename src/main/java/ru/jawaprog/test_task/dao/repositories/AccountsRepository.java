package ru.jawaprog.test_task.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.dao.entities.AccountDTO;

@Repository
public interface AccountsRepository extends JpaRepository<AccountDTO, Long> {
}
