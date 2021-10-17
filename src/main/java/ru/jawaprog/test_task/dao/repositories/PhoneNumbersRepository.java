package ru.jawaprog.test_task.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;

import java.util.List;

@Repository
public interface PhoneNumbersRepository extends JpaRepository<PhoneNumberDTO, Long> {

    List<PhoneNumberDTO> findAllByNumber(String number);

}
