package ru.jawaprog.test_task.web.entities;

import lombok.Getter;
import lombok.Setter;

public class PhoneNumberDTO {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String number;
    @Getter
    @Setter
    private AccountDTO account;
}
