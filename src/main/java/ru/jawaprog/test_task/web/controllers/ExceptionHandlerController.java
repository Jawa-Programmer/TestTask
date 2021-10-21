package ru.jawaprog.test_task.web.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.dao.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.entities.*;

import java.util.HashMap;

@Log4j2
@ControllerAdvice
public class ExceptionHandlerController {
    static private final HashMap<Class, String> classNames = new HashMap<>();

    static {
        classNames.put(Contract.class, "Контракт");
        classNames.put(Client.class, "Клиент");
        classNames.put(Account.class, "Лицевой счёт");
        classNames.put(PhoneNumber.class, "Номер Телефона");
    }

    private <T> ResponseEntity<T> logAndSend(ResponseEntity<T> response, WebRequest request) {
        log.info("Request: " + request + "; Response: " + response);
        return response;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<ErrorInfo> handleNotFound(NotFoundException exception, WebRequest request) {
        return logAndSend(new ResponseEntity<>(
                new ErrorInfo(HttpStatus.NOT_FOUND.value(),
                        classNames.get(exception.getEntityClass()) + " с переданным id не найден"),
                HttpStatus.NOT_FOUND
        ), request);
    }

    @ExceptionHandler(value = {ForeignKeyException.class})
    protected ResponseEntity<ErrorInfo> handleConflict(ForeignKeyException exception, WebRequest request) {
        return logAndSend(new ResponseEntity<>(new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Нарушение ограничения внешнего ключа: " +
                        classNames.get(exception.getEntityClass()) +
                        " с переданным id не найден"),
                HttpStatus.UNPROCESSABLE_ENTITY
        ), request);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    protected ResponseEntity<ErrorInfo> handleConflict(MissingServletRequestParameterException exception, WebRequest request) {
        return logAndSend(new ResponseEntity<>(new ErrorInfo(400,
                "В запросе отсутствует обязательный параметр"),
                HttpStatus.INTERNAL_SERVER_ERROR
        ), request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorInfo> handleConflict(Exception exception, WebRequest request) {
        log.catching(exception);
        return new ResponseEntity<>(new ErrorInfo(500,
                "Внутренняя ошибка сервера"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
