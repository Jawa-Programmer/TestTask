package ru.jawaprog.test_task.exceptions;

import lombok.Getter;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@Getter

@SoapFault(faultCode = FaultCode.SERVER)
public class DataBaseException extends RuntimeException {
    public DataBaseException(String message) {
        super(message);
    }
}
