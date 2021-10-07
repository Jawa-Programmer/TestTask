package ru.jawaprog.test_task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.entities.PhoneNumber;

@Repository
public interface PhoneNumbersRepository extends JpaRepository<PhoneNumber, Long> {

}
