package ru.jawaprog.test_task.web.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.jawaprog.test_task.dao.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.entities.ErrorInfo;
import ru.jawaprog.test_task.web.exceptions.InvalidParamsException;

import static ru.jawaprog.test_task.web.utils.Utils.logAndSend;

@Log4j2
@ControllerAdvice
public class ExceptionHandlerController {
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

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<ErrorInfo> handleNotFound(NotFoundException exception, WebRequest request) {
        return logAndSend(new ResponseEntity<>(
                new ErrorInfo(HttpStatus.NOT_FOUND.value(),
                        EntityType.fromClass(exception.getEntityClass()).name + " с переданным id не найден"),
                HttpStatus.NOT_FOUND
        ), request);
    }

    @ExceptionHandler(value = {InvalidParamsException.class})
    protected ResponseEntity<ErrorInfo> handleInvalidParam(InvalidParamsException exception, WebRequest request) {
        return logAndSend(new ResponseEntity<>(new ErrorInfo(400, exception.getMessage()), HttpStatus.NOT_FOUND), request);
    }

    @ExceptionHandler(value = {ForeignKeyException.class})
    protected ResponseEntity<ErrorInfo> handleConflict(ForeignKeyException exception, WebRequest request) {
        return logAndSend(new ResponseEntity<>(new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Нарушение ограничения внешнего ключа: " +
                        EntityType.fromClass(exception.getEntityClass()).name +
                        " с переданным id не найден"),
                HttpStatus.UNPROCESSABLE_ENTITY
        ), request);
    }

    @ExceptionHandler(value = {javax.validation.ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ErrorInfo> handleInvalidParam2(Exception exception, WebRequest request) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;
            return logAndSend(new ResponseEntity<>(new ErrorInfo(400, "Параметр " + ex.getName() + " имеет некорректное значение"), HttpStatus.BAD_REQUEST), request);
        } else
            return logAndSend(new ResponseEntity<>(new ErrorInfo(400, exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    protected ResponseEntity<ErrorInfo> handleConflict(MissingServletRequestParameterException exception, WebRequest request) {
        return logAndSend(new ResponseEntity<>(new ErrorInfo(400,
                "В запросе отсутствует обязательный параметр"),
                HttpStatus.BAD_REQUEST
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
