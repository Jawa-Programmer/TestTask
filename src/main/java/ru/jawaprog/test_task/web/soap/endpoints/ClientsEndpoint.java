package ru.jawaprog.test_task.web.soap.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.jawaprog.test_task.web.rest.services.ClientsService;
import ru.jawaprog.test_task_mts.*;

import java.util.List;

@Endpoint
public class ClientsEndpoint {
    private static final String NAMESPACE_URI = "http://jawaprog.ru/test-task-mts";

    final private ClientsService service;

    @Autowired
    public ClientsEndpoint(ClientsService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientsRequest")
    @ResponsePayload
    public ClientsListResponse getClients() {
        ClientsListResponse response = new ClientsListResponse();
        response.setClient(List.copyOf(service.findAllSoap()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientRequest")
    @ResponsePayload
    public ClientResponse getClient(@RequestPayload Client request) {
        System.out.println(request.getId());
        ClientResponse response = new ClientResponse();
        response.setClient(service.get(request.getId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findClientsByNameRequest")
    @ResponsePayload
    public ClientsListResponse findClientsByName(@RequestPayload Client request) {
        ClientsListResponse response = new ClientsListResponse();
        response.setClient(List.copyOf(service.findByName(request)));
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findClientsByPhoneRequest")
    @ResponsePayload
    public ClientsListResponse findClientsByPhone(@RequestPayload PhoneNumber request) {
        ClientsListResponse response = new ClientsListResponse();
        response.setClient(List.copyOf(service.findByPhoneNumber(request)));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postClientRequest")
    @ResponsePayload
    public ClientResponse postClient(@RequestPayload Client request) {
        ClientResponse response = new ClientResponse();
        response.setClient(service.saveNew(request));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateClientRequest")
    @ResponsePayload
    public ClientResponse updateClient(@RequestPayload Client request) {
        ClientResponse response = new ClientResponse();
        response.setClient(service.update(request));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteClientRequest")
    @ResponsePayload
    public StatusMessage deleteClient(@RequestPayload DeleteClientRequest request) {
        service.delete(request.getId());
        StatusMessage response = new StatusMessage();
        response.setMessage("Клиент успешно удалён.");
        return response;
    }
}
