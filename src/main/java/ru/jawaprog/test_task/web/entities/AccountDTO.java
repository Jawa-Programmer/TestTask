package ru.jawaprog.test_task.web.entities;

import lombok.Getter;
import lombok.Setter;

public class AccountDTO {
    @Getter
    @Setter
    long id;
    @Getter
    @Setter
    long number;
    @Getter
    @Setter
    ContractDTO contract;
}
