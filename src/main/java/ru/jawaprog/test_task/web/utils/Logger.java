package ru.jawaprog.test_task.web.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@Log4j2
public class Logger {
    public static <T> ResponseEntity<T> logAndSend(ResponseEntity<T> response, WebRequest request) {
        log.info("Request: " + request + "; Response: " + response);
        return response;
    }

}
