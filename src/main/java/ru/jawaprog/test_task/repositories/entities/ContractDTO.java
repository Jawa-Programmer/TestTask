package ru.jawaprog.test_task.repositories.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDTO {
    private Long id;
    private Long clientId;
    private Long number;
}
