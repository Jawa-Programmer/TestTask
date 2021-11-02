package ru.jawaprog.test_task.web.rest.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.web.rest.exceptions.InvalidParamsException;

@Log4j2
@Component
public class Utils {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> ResponseEntity<T> logAndSend(ResponseEntity<T> response, WebRequest request) {
        try {
            log.info("Request: " + request + "; Response: " + objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void validateId(long id) {
        if (id < 1) throw new InvalidParamsException("id должен быть от больше 0");
    }
}
