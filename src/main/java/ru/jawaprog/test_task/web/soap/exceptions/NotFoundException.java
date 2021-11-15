package ru.jawaprog.test_task.web.soap.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName) {
        super(entityName + " с данным id не найден.");
    }
}
