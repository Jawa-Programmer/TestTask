package ru.jawaprog.test_task.web.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.web.exceptions.InvalidParamsException;

@Log4j2
public class Utils {
    public static <T> ResponseEntity<T> logAndSend(ResponseEntity<T> response, WebRequest request) {
        log.info("Request: " + request + "; Response: " + response);
        return response;
    }

    public static void validateId(long id) {
        if (id < 1) throw new InvalidParamsException("id должен быть от больше 0");
    }
}
