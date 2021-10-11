package ru.jawaprog.test_task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.entities.Client;

import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Long> {
    @Query(value = "SELECT * FROM clients WHERE full_name ILIKE %?1%", nativeQuery = true)
    List<Client> findAllByName(String name);
}
