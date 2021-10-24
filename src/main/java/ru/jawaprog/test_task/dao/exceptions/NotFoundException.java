package ru.jawaprog.test_task.dao.exceptions;

public class NotFoundException extends DataBaseException {

    public NotFoundException(Class entityClass) {
        super(entityClass);
    }
}
