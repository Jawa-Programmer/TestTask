package ru.jawaprog.test_task.dao.exceptions;

import lombok.Getter;

public class NotFoundException extends DataBaseException {

    public NotFoundException(Class entityClass) {
        super(entityClass);
    }
}
