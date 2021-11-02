package ru.jawaprog.test_task.web.soap.endpoints;


import io.spring.guides.gs_producing_web_service.GetClientRequest;
import io.spring.guides.gs_producing_web_service.GetClientResponse;
import io.spring.guides.gs_producing_web_service.GetClientsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.jawaprog.test_task.web.soap.services.ClientsSoapService;

import java.util.List;

@Endpoint
public class ClientsEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private ClientsSoapService service;

    @Autowired
    public ClientsEndpoint(ClientsSoapService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientsRequest")
    @ResponsePayload
    public GetClientsResponse getClients() {
        GetClientsResponse response = new GetClientsResponse();
        response.setClients(List.copyOf(service.findAll()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientRequest")
    @ResponsePayload
    public GetClientResponse getClient(@RequestPayload GetClientRequest request) {
        System.out.println(request.getId());
        GetClientResponse response = new GetClientResponse();
        response.setClient(service.get(request.getId()));
        return response;
    }
}
