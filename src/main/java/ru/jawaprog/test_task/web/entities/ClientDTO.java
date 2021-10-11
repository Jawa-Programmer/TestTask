package ru.jawaprog.test_task.web.entities;

import lombok.Getter;
import lombok.Setter;

public class ClientDTO {
    public enum ClientType {INDIVIDUAL, ENTITY}
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String fullName;
    @Getter
    @Setter
    private ClientType type;

}
