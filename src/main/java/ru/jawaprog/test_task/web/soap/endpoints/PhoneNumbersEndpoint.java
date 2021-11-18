package ru.jawaprog.test_task.web.soap.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.jawaprog.test_task.services.PhoneNumbersService;
import ru.jawaprog.test_task_mts.*;

import java.util.List;

@Endpoint
public class PhoneNumbersEndpoint {
    private static final String NAMESPACE_URI = "http://jawaprog.ru/test-task-mts";

    final private PhoneNumbersService service;


    @Autowired
    public PhoneNumbersEndpoint(PhoneNumbersService service) {
        this.service = service;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPhoneNumbersRequest")
    @ResponsePayload
    public PhoneNumbersListResponse getPhoneNumbers() {
        PhoneNumbersListResponse response = new PhoneNumbersListResponse();
        response.setPhoneNumber(List.copyOf(service.findAllSoap()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPhoneNumberRequest")
    @ResponsePayload
    public PhoneNumberResponse getPhoneNumber(@RequestPayload PhoneNumber request) {
        PhoneNumberResponse response = new PhoneNumberResponse();
        response.setPhoneNumber(service.get(request));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postPhoneNumberRequest")
    @ResponsePayload
    public PhoneNumberResponse postPhoneNumber(@RequestPayload PhoneNumber request) {
        PhoneNumberResponse response = new PhoneNumberResponse();
        response.setPhoneNumber(service.saveNew(request));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updatePhoneNumberRequest")
    @ResponsePayload
    public PhoneNumberResponse updatePhoneNumber(@RequestPayload PhoneNumber request) {
        PhoneNumberResponse response = new PhoneNumberResponse();
        response.setPhoneNumber(service.update(request));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePhoneNumberRequest")
    @ResponsePayload
    public StatusMessage deletePhoneNumber(@RequestPayload DeletePhoneNumberRequest request) {
        service.delete(request.getId());
        StatusMessage response = new StatusMessage();
        response.setMessage("Номер телефона успешно удалён.");
        return response;
    }
}