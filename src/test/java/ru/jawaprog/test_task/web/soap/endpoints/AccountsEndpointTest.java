package ru.jawaprog.test_task.web.soap.endpoints;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.jawaprog.test_task.configuration.WebServiceConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
@ContextConfiguration(classes = {TestConfig.class, WebServiceConfig.class})
class AccountsEndpointTest {
/*
    @Autowired
    private AccountsService service;

    @Autowired
    private MockWebServiceClient mockClient;

    @Test
    void getAccounts() {
        Account c1 = new Account(), c2 = new Account();
        c1.setId(1L);
        c1.setNumber(786L);
        c1.setContractId(1L);
        c2.setId(2L);
        c2.setNumber(788L);
        c2.setContractId(1L);

        Mockito.when(service.findAllSoap()).thenReturn(List.of(c1, c2));

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
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
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
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
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
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Счёт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updateAccountRequest id='1' contractId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
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
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Счёт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

 */
}