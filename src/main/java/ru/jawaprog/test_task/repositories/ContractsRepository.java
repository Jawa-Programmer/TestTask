package ru.jawaprog.test_task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.entities.Contract;

@Repository
public interface ContractsRepository extends JpaRepository<Contract, Long> {
}
