package ru.jawaprog.test_task.exceptions;

public class InvalidParamsException extends RuntimeException {
    public InvalidParamsException(String message) {
        super(message);
    }
}
