package ru.jawaprog.test_task.web.soap.endpoints;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.jawaprog.test_task.web.soap.services.ClientsSoapService;
import ru.jawaprog.test_task_mts.*;

import java.util.List;

@Endpoint
public class ClientsEndpoint {
    private static final String NAMESPACE_URI = "http://jawaprog.ru/test-task-mts";

    final private ClientsSoapService service;

    @Autowired
    public ClientsEndpoint(ClientsSoapService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientsRequest")
    @ResponsePayload
    public ClientsListResponse getClients() {
        ClientsListResponse response = new ClientsListResponse();
        response.setClient(List.copyOf(service.findAll()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientRequest")
    @ResponsePayload
    public ClientResponse getClient(@RequestPayload GetClientRequest request) {
        System.out.println(request.getId());
        ClientResponse response = new ClientResponse();
        response.setClient(service.get(request.getId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findClientsByNameRequest")
    @ResponsePayload
    public ClientsListResponse findClientsByName(@RequestPayload FindClientsByNameRequest request) {
        ClientsListResponse response = new ClientsListResponse();
        response.setClient(List.copyOf(service.findByName(request.getName())));
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findClientsByPhoneRequest")
    @ResponsePayload
    public ClientsListResponse findClientsByPhone(@RequestPayload FindClientsByPhoneRequest request) {
        ClientsListResponse response = new ClientsListResponse();
        response.setClient(List.copyOf(service.findByPhoneNumber(request.getNumber())));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postClientRequest")
    @ResponsePayload
    public ClientResponse postClient(@RequestPayload PostClientRequest request) {
        ClientResponse response = new ClientResponse();
        response.setClient(service.saveNew(request.getFullName(), request.getType()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateClientRequest")
    @ResponsePayload
    public ClientResponse updateClient(@RequestPayload UpdateClientRequest request) {
        ClientResponse response = new ClientResponse();
        response.setClient(service.update(request.getId(), request.getFullName(), request.getType()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteClientRequest")
    @ResponsePayload
    public StatusMessage deleteClient(@RequestPayload DeleteClientRequest request) {
        service.delete(request.getId());
        StatusMessage response = new StatusMessage();
        response.setMessage("Client successful deleted");
        return response;
    }
}
