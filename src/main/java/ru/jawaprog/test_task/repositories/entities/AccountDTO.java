package ru.jawaprog.test_task.repositories.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private Long id;
    private Long contractId;
    private Long number;
}
