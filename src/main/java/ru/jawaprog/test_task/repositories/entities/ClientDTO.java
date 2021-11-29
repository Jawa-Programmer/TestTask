package ru.jawaprog.test_task.repositories.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {
    private Long id;
    private String fullName;
    private Integer type;
}
