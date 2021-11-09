package ru.jawaprog.test_task.web.rest.exceptions;

public class NotFoundException extends DataBaseException {

    public NotFoundException(Class entityClass) {
        super(entityClass);
    }
}
