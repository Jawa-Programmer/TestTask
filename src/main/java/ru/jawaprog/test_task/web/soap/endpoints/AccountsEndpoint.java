package ru.jawaprog.test_task.web.soap.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.jawaprog.test_task.web.soap.services.AccountsSoapService;
import ru.jawaprog.test_task_mts.*;

import java.util.List;

@Endpoint
public class AccountsEndpoint {
    private static final String NAMESPACE_URI = "http://jawaprog.ru/test-task-mts";

    final private AccountsSoapService service;

    @Autowired
    public AccountsEndpoint(AccountsSoapService service) {
        this.service = service;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAccountsRequest")
    @ResponsePayload
    public AccountsListResponse getAccounts() {
        AccountsListResponse response = new AccountsListResponse();
        response.setAccount(List.copyOf(service.findAll()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAccountRequest")
    @ResponsePayload
    public AccountResponse getAccount(@RequestPayload GetAccountRequest request) {
        AccountResponse response = new AccountResponse();
        response.setAccount(service.get(request.getId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postAccountRequest")
    @ResponsePayload
    public AccountResponse postAccount(@RequestPayload PostAccountRequest request) {
        AccountResponse response = new AccountResponse();
        response.setAccount(service.saveNew(request.getNumber(), request.getContractId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateAccountRequest")
    @ResponsePayload
    public AccountResponse updateAccount(@RequestPayload UpdateAccountRequest request) {
        AccountResponse response = new AccountResponse();
        response.setAccount(service.update(request.getId(), request.getNumber(), request.getContractId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAccountRequest")
    @ResponsePayload
    public StatusMessage deleteAccount(@RequestPayload DeleteAccountRequest request) {
        service.delete(request.getId());
        StatusMessage response = new StatusMessage();
        response.setMessage("Account successful deleted");
        return response;
    }
}
