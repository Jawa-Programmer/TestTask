package ru.jawaprog.test_task.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.dao.entities.ClientDTO;

import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<ClientDTO, Long> {
    @Query(value = "SELECT * FROM clients WHERE full_name ILIKE %?1%", nativeQuery = true)
    List<ClientDTO> findAllByName(String name);
}
