package ru.jawaprog.test_task.dao.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberDTO {
    private long id;
    private String number;
    private long accountId;
}
