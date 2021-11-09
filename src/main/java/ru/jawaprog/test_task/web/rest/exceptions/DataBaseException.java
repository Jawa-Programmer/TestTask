package ru.jawaprog.test_task.web.rest.exceptions;

import lombok.Getter;

public class DataBaseException extends RuntimeException{
    @Getter
    private Class entityClass;

    public DataBaseException(Class entityClass) {
        this.entityClass = entityClass;
    }
}
