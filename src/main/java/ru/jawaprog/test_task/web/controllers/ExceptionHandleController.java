package ru.jawaprog.test_task.web.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandleController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {NoSuchElementException.class, EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleNotFound(
            RuntimeException ex,
            WebRequest request) {
        return handleExceptionInternal(ex, "Запись с переданным id не найдена", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex,
            WebRequest request) {
        return handleExceptionInternal(ex, "Нарушено ограничение внешнего ключа", new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
