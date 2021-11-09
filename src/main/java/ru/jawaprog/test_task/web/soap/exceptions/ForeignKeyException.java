package ru.jawaprog.test_task.web.soap.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT)
public class ForeignKeyException extends RuntimeException {
    public ForeignKeyException(String entityName) {
        super("Ошибка внешнего ключа. "+entityName + " с переданным id не найден.");
    }
}