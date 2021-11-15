package ru.jawaprog.test_task.web.soap.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;
import ru.jawaprog.test_task.web.rest.services.AccountsService;
import ru.jawaprog.test_task.web.rest.services.ClientsService;
import ru.jawaprog.test_task.web.rest.services.ContractsService;
import ru.jawaprog.test_task.web.rest.services.PhoneNumbersService;
import ru.jawaprog.test_task.web.soap.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.soap.services.AccountsSoapService;
import ru.jawaprog.test_task.web.soap.services.ClientsSoapService;
import ru.jawaprog.test_task.web.soap.services.ContractsSoapService;
import ru.jawaprog.test_task.web.soap.services.PhoneNumbersSoapService;
import ru.jawaprog.test_task.web.utils.Utils;
import ru.jawaprog.test_task_mts.Account;

import javax.xml.transform.Source;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
class AccountsEndpointTest {
    @TestConfiguration
    @ComponentScan("ru.jawaprog.test_task.web.soap.endpoints")
    static class TestConfig {
        @Bean
        Utils getUtils(ObjectMapper objectMapper) {
            return new Utils(objectMapper);
        }

        @Bean()
        MockWebServiceClient mockClient(ApplicationContext context) {
            return MockWebServiceClient.createClient(context);
        }

    }

    @MockBean
    private AccountsSoapService service;

    @Autowired
    private MockWebServiceClient mockClient;


    // Нижележащие бины созданы потому что без них тест не запускается, хотя по факту они нигде не используются в тесте


    @MockBean
    private ClientsSoapService clientsSoapService;
    @MockBean
    private ContractsSoapService contractsSoapService;
    @MockBean
    private PhoneNumbersSoapService phoneNumbersSoapService;

    // эти бины вообще нужны только REST контроллеру, но без них mockClient тоже не может собраться
    @MockBean
    private ClientsService clientsService;
    @MockBean
    private AccountsService accountsService;
    @MockBean
    private ContractsService contractsService;
    @MockBean
    private PhoneNumbersService phoneNumbersService;

    @Test
    void getAccounts() {
        Account c1 = new Account(), c2 = new Account();
        c1.setId(1L);
        c1.setNumber(786L);
        c1.setContractId(1L);
        c2.setId(2L);
        c2.setNumber(788L);
        c2.setContractId(1L);

        Mockito.when(service.findAll()).thenReturn(List.of(c1, c2));

        Source requestPayload = new StringSource(
                "<getAccountsRequest xmlns = 'http://jawaprog.ru/test-task-mts'/>");
        Source responsePayload = new StringSource(
                "<AccountsListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                        "<account id='1' contractId='1' number='786'/>" +
                        "<account id='2' contractId='1' number='788'/>" +
                        "</AccountsListResponse>");
        mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    void getAccount() {
        Account c1 = new Account();
        c1.setId(1L);
        c1.setNumber(786L);
        c1.setContractId(1L);

        Mockito.when(service.get(1)).thenReturn(c1);
        Mockito.when(service.get(2)).thenThrow(new NotFoundException("Счёт"));
        {
            Source requestPayload = new StringSource(
                    "<getAccountRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<AccountResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<account id='1' contractId='1' number='786'/>" +
                            "</AccountResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<getAccountRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Счёт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void postAccount() {
        Account c1 = new Account();
        c1.setId(2L);
        c1.setNumber(786L);
        c1.setContractId(1L);

        Mockito.when(service.saveNew(786, 1)).thenReturn(c1);
        Mockito.when(service.saveNew(787, 3)).thenThrow(new ForeignKeyException("Контракт"));
        {
            Source requestPayload = new StringSource(
                    "<postAccountRequest contractId='1' number='786' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<AccountResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<account id='2' contractId='1' number='786'/>" +
                            "</AccountResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<postAccountRequest contractId='3' number='787' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Контракт с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void updateAccount() {
        Account c1 = new Account();
        c1.setId(1L);
        c1.setNumber(787L);
        c1.setContractId(1L);

        Mockito.when(service.update(1, 787L, null)).thenReturn(c1);
        Mockito.when(service.update(2, null, 2L)).thenThrow(new NotFoundException("Счёт"));
        Mockito.when(service.update(1, null, 2L)).thenThrow(new ForeignKeyException("Контракт"));
        {
            Source requestPayload = new StringSource(
                    "<updateAccountRequest id='1' number='787' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<AccountResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<account id='1' contractId='1' number='787'/>" +
                            "</AccountResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updateAccountRequest id='2' contractId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Счёт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updateAccountRequest id='1' contractId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Контракт с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void deleteAccount() {
        Mockito.doNothing().when(service).delete(1);
        Mockito.doThrow(new NotFoundException("Счёт")).when(service).delete(2);
        {
            Source requestPayload = new StringSource(
                    "<deleteAccountRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<StatusMessage xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<message>Счёт успешно удалён.</message>" +
                            "</StatusMessage>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<deleteAccountRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Счёт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }
}