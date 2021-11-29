package ru.jawaprog.test_task.repositories.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberDTO {
    private Long id;
    private String number;
    private Long accountId;
}
