package ru.jawaprog.test_task.web.rest.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.jawaprog.test_task.exceptions.DataBaseException;
import ru.jawaprog.test_task.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.exceptions.InvalidParamsException;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.entities.ErrorInfo;
import ru.jawaprog.test_task.web.utils.Utils;

@Log4j2
@ControllerAdvice
public class ExceptionHandlerController {
    private final Utils utils;

    @Autowired
    public ExceptionHandlerController(Utils utils) {
        this.utils = utils;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<ErrorInfo> handleNotFound(NotFoundException exception, WebRequest request) {
        return utils.logAndSend(new ResponseEntity<>(
                new ErrorInfo(HttpStatus.NOT_FOUND.value(),
                        exception.getMessage()),
                HttpStatus.NOT_FOUND
        ), request);
    }

    @ExceptionHandler(value = {InvalidParamsException.class})
    protected ResponseEntity<ErrorInfo> handleInvalidParam(InvalidParamsException exception, WebRequest request) {
        return utils.logAndSend(new ResponseEntity<>(new ErrorInfo(400, exception.getMessage()), HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(value = {ForeignKeyException.class})
    protected ResponseEntity<ErrorInfo> handleConflict(ForeignKeyException exception, WebRequest request) {
        return utils.logAndSend(new ResponseEntity<>(new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getLocalizedMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY
        ), request);
    }

    @ExceptionHandler(value = {javax.validation.ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ErrorInfo> handleInvalidParam2(Exception exception, WebRequest request) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;
            return utils.logAndSend(new ResponseEntity<>(new ErrorInfo(400, "Параметр " + ex.getName() + " имеет некорректное значение"), HttpStatus.BAD_REQUEST), request);
        } else
            return utils.logAndSend(new ResponseEntity<>(new ErrorInfo(400, exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    protected ResponseEntity<ErrorInfo> handleConflict(MissingServletRequestParameterException exception, WebRequest request) {
        return utils.logAndSend(new ResponseEntity<>(new ErrorInfo(400,
                "В запросе отсутствует обязательный параметр"),
                HttpStatus.BAD_REQUEST
        ), request);
    }

    @ExceptionHandler(value = {DataBaseException.class})
    protected ResponseEntity<ErrorInfo> handleConflict(DataBaseException exception, WebRequest request) {
        log.catching(exception);
        return utils.logAndSend(new ResponseEntity<>(new ErrorInfo(500,
                "Ошибка обращения к базе данных"),
                HttpStatus.INTERNAL_SERVER_ERROR
        ), request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorInfo> handleConflict(Exception exception, WebRequest request) {
        log.catching(exception);
        return utils.logAndSend(new ResponseEntity<>(new ErrorInfo(500,
                "Внутренняя ошибка сервера"),
                HttpStatus.INTERNAL_SERVER_ERROR
        ), request);
    }

    private enum EntityType {
        CLIENT("Клиент"), CONTRACT("Контракт"), ACCOUNT("Лицевой счёт"), PHONE_NUMBER("Номер Телефона");
        final String name;

        EntityType(String nm) {
            name = nm;
        }

        public static EntityType fromClass(Class cls) {
            if (cls == Client.class) return CLIENT;
            else if (cls == Contract.class) return CONTRACT;
            else if (cls == Account.class) return ACCOUNT;
            else return PHONE_NUMBER;
        }
    }
}
