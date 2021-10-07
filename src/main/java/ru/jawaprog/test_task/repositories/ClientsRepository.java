package ru.jawaprog.test_task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.entities.Client;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Long> {
}
