package ru.jawaprog.test_task.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.exceptions.InvalidParamsException;

import java.util.Iterator;

@Log4j2
@Component
public class Utils {

    private final ObjectMapper objectMapper;

    @Autowired
    public Utils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> ResponseEntity<T> logAndSend(ResponseEntity<T> response, WebRequest request) {
        try {
            StringBuilder strb = new StringBuilder();
            for (Iterator<String> it = request.getParameterNames(); it.hasNext(); ) {
                String n = it.next();
                strb.append(n).append("=[");
                for (String val : request.getParameterValues(n)) {
                    strb.append(val).append(',');
                }
                strb.append("]");
            }
            log.info("Request: " + request + "; Request Parameters: " + strb + "; Response: " + objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void validateId(long id) {
        if (id < 1) throw new InvalidParamsException("id должен быть от больше 0");
    }
}
