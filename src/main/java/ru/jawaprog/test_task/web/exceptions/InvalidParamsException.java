package ru.jawaprog.test_task.web.exceptions;

public class InvalidParamsException extends RuntimeException {
    public InvalidParamsException(String message) {
        super(message);
    }
}
