package ru.jawaprog.test_task.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.dao.entities.ContractDAO;

@Repository
public interface ContractsRepository extends JpaRepository<ContractDAO, Long> {
}
