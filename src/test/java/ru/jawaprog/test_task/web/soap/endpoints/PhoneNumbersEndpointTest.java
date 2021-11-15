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
import ru.jawaprog.test_task_mts.PhoneNumber;

import javax.xml.transform.Source;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
class PhoneNumbersEndpointTest {
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
    private PhoneNumbersSoapService service;

    @Autowired
    private MockWebServiceClient mockClient;


    // Нижележащие бины созданы потому что без них тест не запускается, хотя по факту они нигде не используются в тесте


    @MockBean
    private ClientsSoapService clientsSoapService;
    @MockBean
    private ContractsSoapService contractsSoapService;
    @MockBean
    private AccountsSoapService accountsSoapService;

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
    void getPhoneNumbers() {
        PhoneNumber c1 = new PhoneNumber(), c2 = new PhoneNumber();
        c1.setId(1L);
        c1.setNumber("+7 (912) 555-33-22");
        c1.setAccountId(1L);
        c2.setId(2L);
        c2.setNumber("+7 (800) 555-35-35");
        c2.setAccountId(1L);

        Mockito.when(service.findAll()).thenReturn(List.of(c1, c2));

        Source requestPayload = new StringSource(
                "<getPhoneNumbersRequest xmlns = 'http://jawaprog.ru/test-task-mts'/>");
        Source responsePayload = new StringSource(
                "<PhoneNumbersListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                        "<phoneNumber id='1' accountId='1' number='+7 (912) 555-33-22'/>" +
                        "<phoneNumber id='2' accountId='1' number='+7 (800) 555-35-35'/>" +
                        "</PhoneNumbersListResponse>");
        mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    void getPhoneNumber() {
        PhoneNumber c1 = new PhoneNumber();
        c1.setId(1L);
        c1.setNumber("+7 (912) 555-33-22");
        c1.setAccountId(1L);

        Mockito.when(service.get(1)).thenReturn(c1);
        Mockito.when(service.get(2)).thenThrow(new NotFoundException("Номер телефона"));
        {
            Source requestPayload = new StringSource(
                    "<getPhoneNumberRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='1' accountId='1' number='+7 (912) 555-33-22'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<getPhoneNumberRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Номер телефона с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void postPhoneNumber() {
        PhoneNumber c1 = new PhoneNumber();
        c1.setId(2L);
        c1.setNumber("+7 (912) 555-33-22");
        c1.setAccountId(1L);

        Mockito.when(service.saveNew("+7 (912) 555-33-22", 1)).thenReturn(c1);
        Mockito.when(service.saveNew("+7 (911) 222-33-44", 3)).thenThrow(new ForeignKeyException("Счёт"));
        {
            Source requestPayload = new StringSource(
                    "<postPhoneNumberRequest accountId='1' number='+7 (912) 555-33-22' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='2' accountId='1' number='+7 (912) 555-33-22'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<postPhoneNumberRequest accountId='3' number='+7 (911) 222-33-44' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Счёт с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void updatePhoneNumber() {
        PhoneNumber c1 = new PhoneNumber();
        c1.setId(1L);
        c1.setNumber("+7 (911) 222-33-44");
        c1.setAccountId(1L);

        Mockito.when(service.update(1, "+7 (911) 222-33-44", null)).thenReturn(c1);
        Mockito.when(service.update(2, null, 2L)).thenThrow(new NotFoundException("Номер телефона"));
        Mockito.when(service.update(1, null, 2L)).thenThrow(new ForeignKeyException("Счёт"));
        {
            Source requestPayload = new StringSource(
                    "<updatePhoneNumberRequest id='1' number='+7 (911) 222-33-44' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='1' accountId='1' number='+7 (911) 222-33-44'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updatePhoneNumberRequest id='2' accountId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Номер телефона с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updatePhoneNumberRequest id='1' accountId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Счёт с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void deletePhoneNumber() {
        Mockito.doNothing().when(service).delete(1);
        Mockito.doThrow(new NotFoundException("Номер телефона")).when(service).delete(2);
        {
            Source requestPayload = new StringSource(
                    "<deletePhoneNumberRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<StatusMessage xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<message>Номер телефона успешно удалён.</message>" +
                            "</StatusMessage>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<deletePhoneNumberRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Server</faultcode>" +
                            "<faultstring xml:lang='en'>Номер телефона с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }
}