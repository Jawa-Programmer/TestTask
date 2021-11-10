package ru.jawaprog.test_task.dao.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {
    private long id;
    private String fullName;
    private int type;
}
