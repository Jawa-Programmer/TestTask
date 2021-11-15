package ru.jawaprog.test_task.web.soap.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
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
import ru.jawaprog.test_task.web.soap.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.soap.services.AccountsSoapService;
import ru.jawaprog.test_task.web.soap.services.ClientsSoapService;
import ru.jawaprog.test_task.web.soap.services.ContractsSoapService;
import ru.jawaprog.test_task.web.soap.services.PhoneNumbersSoapService;
import ru.jawaprog.test_task.web.utils.Utils;
import ru.jawaprog.test_task_mts.Client;
import ru.jawaprog.test_task_mts.ClientType;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
class ClientsEndpointTest {

    @TestConfiguration
    @ComponentScan("ru.jawaprog.test_task.web.soap.endpoints")
    static class TestConfig {
        @Bean
        Utils getUtils(ObjectMapper objectMapper) {
            return new Utils(objectMapper);
        }

        @Bean
        MockWebServiceClient mockClient(ApplicationContext context) {
            return MockWebServiceClient.createClient(context);
        }

    }

    @MockBean
    private ClientsSoapService service;

    @Autowired
    private MockWebServiceClient mockClient;


    // Нижележащие бины созданы потому что без них тест не запускается, хотя по факту они нигде не используются в тесте


    @MockBean
    private ContractsSoapService contractsSoapService;
    @MockBean
    private AccountsSoapService accountsSoapService;
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
    void getClients() {
        Client c1 = new Client(), c2 = new Client();
        c1.setId(1L);
        c1.setFullName("Иванов И. И.");
        c1.setType(ClientType.INDIVIDUAL);
        c1.setContract(null);
        c2.setId(2L);
        c2.setFullName("ОАО \"Общество Гигантских растений\"");
        c2.setType(ClientType.ENTITY);
        c2.setContract(null);

        Mockito.when(service.findAll()).thenReturn(List.of(c1, c2));

        Source requestPayload = new StringSource(
                "<getClientsRequest xmlns = 'http://jawaprog.ru/test-task-mts'/>");
        Source responsePayload = new StringSource(
                "<ClientsListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                        "<client fullName='Иванов И. И.' id='1' type='INDIVIDUAL'/>" +
                        "<client fullName='ОАО \"Общество Гигантских растений\"' id='2' type='ENTITY'/>" +
                        "</ClientsListResponse>");
        mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));

    }

    @Test
    void getClient() {
        Client c1 = new Client();
        c1.setId(1L);
        c1.setFullName("Иванов И. И.");
        c1.setType(ClientType.INDIVIDUAL);
        c1.setContract(null);
        Mockito.when(service.get(1)).thenReturn(c1);
        Mockito.when(service.get(2)).thenThrow(new NotFoundException("Клиент"));
        {
            Source requestPayload = new StringSource(
                    "<getClientRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ClientResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<client fullName='Иванов И. И.' id='1' type='INDIVIDUAL'/>" +
                            "</ClientResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }

        {
            Source requestPayload = new StringSource(
                    "<getClientRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Клиент с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void findClientsByName() {
        Client c1 = new Client();
        c1.setId(1L);
        c1.setFullName("Иванов И. И.");
        c1.setType(ClientType.INDIVIDUAL);
        c1.setContract(null);
        Mockito.when(service.findByName("Ив")).thenReturn(List.of(c1));
        {
            Source requestPayload = new StringSource(
                    "<findClientsByNameRequest name='Ив' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ClientsListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<client fullName='Иванов И. И.' id='1' type='INDIVIDUAL'/>" +
                            "</ClientsListResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void findClientsByPhone() {
        Mockito.when(service.findByPhoneNumber(ArgumentMatchers.any())).thenReturn(new ArrayList<>());
        {
            Source requestPayload = new StringSource(
                    "<findClientsByNameRequest phone='+7 (800) 555-35-35' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ClientsListResponse xmlns='http://jawaprog.ru/test-task-mts'/>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void postClient() {
        Client c1 = new Client();
        c1.setId(3L);
        c1.setFullName("Зубенко Михаил Петрович");
        c1.setType(ClientType.INDIVIDUAL);
        c1.setContract(null);
        Mockito.when(service.saveNew("Зубенко Михаил Петрович", ClientType.INDIVIDUAL)).thenReturn(c1);
        {
            Source requestPayload = new StringSource(
                    "<postClientRequest fullName='Зубенко Михаил Петрович' type='INDIVIDUAL' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ClientResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<client id='3' fullName='Зубенко Михаил Петрович' type='INDIVIDUAL'/>" +
                            "</ClientResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void updateClient() {
        Client c1 = new Client();
        c1.setId(1L);
        c1.setFullName("Иванов И. А.");
        c1.setType(ClientType.INDIVIDUAL);
        c1.setContract(null);
        Mockito.when(service.update(1, "Иванов И. А.", null)).thenReturn(c1);
        Mockito.when(service.update(2, null, ClientType.ENTITY)).thenThrow(new NotFoundException("Клиент"));
        {
            Source requestPayload = new StringSource(
                    "<updateClientRequest id='1' fullName='Иванов И. А.' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ClientResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<client fullName='Иванов И. А.' id='1' type='INDIVIDUAL'/>" +
                            "</ClientResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updateClientRequest id='2' type='ENTITY' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Клиент с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void deleteClient() {
        Mockito.doNothing().when(service).delete(1);
        Mockito.doThrow(new NotFoundException("Клиент")).when(service).delete(2);
        {
            Source requestPayload = new StringSource(
                    "<deleteClientRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<StatusMessage xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<message>Счёт успешно удалён.</message>" +
                            "</StatusMessage>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<deleteClientRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Клиент с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }
}