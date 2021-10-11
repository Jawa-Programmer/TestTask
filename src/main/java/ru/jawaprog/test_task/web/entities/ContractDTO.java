package ru.jawaprog.test_task.web.entities;

import lombok.Getter;
import lombok.Setter;

public class ContractDTO {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private long number;
    @Getter
    @Setter
    ClientDTO client;
}
