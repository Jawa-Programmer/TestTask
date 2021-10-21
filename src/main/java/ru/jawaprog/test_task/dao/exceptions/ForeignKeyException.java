package ru.jawaprog.test_task.dao.exceptions;

public class ForeignKeyException extends DataBaseException {
    public ForeignKeyException(Class entityClass) {
        super(entityClass);
    }
}
