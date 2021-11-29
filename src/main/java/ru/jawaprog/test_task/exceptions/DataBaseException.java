package ru.jawaprog.test_task.exceptions;

import lombok.Getter;

@Getter
public class DataBaseException extends RuntimeException {
    public DataBaseException(String message) {
        super(message);
    }
}
