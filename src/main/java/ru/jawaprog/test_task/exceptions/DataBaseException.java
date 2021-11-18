package ru.jawaprog.test_task.exceptions;

import lombok.Getter;

public class DataBaseException extends RuntimeException {
    @Getter
    private Class entityClass;

    public DataBaseException(Class entityClass) {
        this.entityClass = entityClass;
    }
}
