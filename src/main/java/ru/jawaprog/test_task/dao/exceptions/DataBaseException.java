package ru.jawaprog.test_task.dao.exceptions;

import lombok.Getter;

public class DataBaseException extends RuntimeException{
    @Getter
    private Class entityClass;

    public DataBaseException(Class entityClass) {
        this.entityClass = entityClass;
    }
}
