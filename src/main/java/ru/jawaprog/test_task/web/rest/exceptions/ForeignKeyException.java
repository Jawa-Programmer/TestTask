package ru.jawaprog.test_task.web.rest.exceptions;

public class ForeignKeyException extends DataBaseException {
    public ForeignKeyException(Class entityClass) {
        super(entityClass);
    }
}
