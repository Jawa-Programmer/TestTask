package ru.jawaprog.test_task.web.soap.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.jawaprog.test_task.web.soap.services.ContractsSoapService;
import ru.jawaprog.test_task.web.utils.Utils;
import ru.jawaprog.test_task_mts.*;

import java.util.List;

@Endpoint
public class ContractsEndpoint {
    private static final String NAMESPACE_URI = "http://jawaprog.ru/test-task-mts";

    final private ContractsSoapService service;

    @Autowired
    public ContractsEndpoint(ContractsSoapService service, Utils utils) {
        this.service = service;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getContractsRequest")
    @ResponsePayload
    public ContractsListResponse getContracts() {
        ContractsListResponse response = new ContractsListResponse();
        response.setContract(List.copyOf(service.findAll()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getContractRequest")
    @ResponsePayload
    public ContractResponse getContract(@RequestPayload GetContractRequest request) {
        ContractResponse response = new ContractResponse();
        response.setContract(service.get(request.getId()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postContractRequest")
    @ResponsePayload
    public ContractResponse postContract(@RequestPayload PostContractRequest request) {
        ContractResponse response = new ContractResponse();
        response.setContract(service.saveNew(request.getNumber(), request.getClientId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateContractRequest")
    @ResponsePayload
    public ContractResponse updateContract(@RequestPayload UpdateContractRequest request) {
        ContractResponse response = new ContractResponse();
        response.setContract(service.update(request.getId(), request.getNumber(), request.getClientId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteContractRequest")
    @ResponsePayload
    public StatusMessage deleteContract(@RequestPayload DeleteContractRequest request) {
        service.delete(request.getId());
        StatusMessage response = new StatusMessage();
        response.setMessage("Contract successful deleted");
        return response;
    }
}
