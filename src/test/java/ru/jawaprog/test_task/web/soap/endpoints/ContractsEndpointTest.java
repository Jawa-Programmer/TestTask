package ru.jawaprog.test_task.web.soap.endpoints;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.jawaprog.test_task.configuration.WebServiceConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
@ContextConfiguration(classes = {TestConfig.class, WebServiceConfig.class})
class ContractsEndpointTest {
/*
    @Autowired
    private ContractsService service;

    @Autowired
    private MockWebServiceClient mockClient;

    @Test
    void getContracts() {
        Contract c1 = new Contract(), c2 = new Contract();
        c1.setId(1L);
        c1.setNumber(786L);
        c1.setClientId(1L);
        c2.setId(2L);
        c2.setNumber(788L);
        c2.setClientId(1L);

        Mockito.when(service.findAllSoap()).thenReturn(List.of(c1, c2));

        Source requestPayload = new StringSource(
                "<getContractsRequest xmlns = 'http://jawaprog.ru/test-task-mts'/>");
        Source responsePayload = new StringSource(
                "<ContractsListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                        "<contract id='1' clientId='1' number='786'/>" +
                        "<contract id='2' clientId='1' number='788'/>" +
                        "</ContractsListResponse>");
        mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    void getContract() {
        Contract c1 = new Contract();
        c1.setId(1L);
        c1.setNumber(786L);
        c1.setClientId(1L);

        Mockito.when(service.get(1)).thenReturn(c1);
        Mockito.when(service.get(2)).thenThrow(new NotFoundException("Контракт"));
        {
            Source requestPayload = new StringSource(
                    "<getContractRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ContractResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<contract id='1' clientId='1' number='786'/>" +
                            "</ContractResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<getContractRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Контракт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void postContract() {
        Contract c1 = new Contract();
        c1.setId(2L);
        c1.setNumber(786L);
        c1.setClientId(1L);

        Mockito.when(service.saveNew(786, 1)).thenReturn(c1);
        Mockito.when(service.saveNew(789, 2)).thenThrow(new ForeignKeyException("Клиент"));
        {
            Source requestPayload = new StringSource(
                    "<postContractRequest clientId='1' number='786' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ContractResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<contract id='2' clientId='1' number='786'/>" +
                            "</ContractResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<postContractRequest clientId='2' number='789' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Клиент с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void updateContract() {
        Contract c1 = new Contract();
        c1.setId(1L);
        c1.setNumber(787L);
        c1.setClientId(1L);

        Mockito.when(service.update(1, 787L, null)).thenReturn(c1);
        Mockito.when(service.update(2, null, 2L)).thenThrow(new NotFoundException("Контракт"));
        Mockito.when(service.update(1, null, 2L)).thenThrow(new ForeignKeyException("Клиент"));
        {
            Source requestPayload = new StringSource(
                    "<updateContractRequest id='1' number='787' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ContractResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<contract id='1' clientId='1' number='787'/>" +
                            "</ContractResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updateContractRequest id='2' clientId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Контракт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updateContractRequest id='1' clientId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Клиент с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void deleteContract() {
        Mockito.doNothing().when(service).delete(1);
        Mockito.doThrow(new NotFoundException("Контракт")).when(service).delete(2);
        {
            Source requestPayload = new StringSource(
                    "<deleteContractRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<StatusMessage xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<message>Контракт успешно удалён.</message>" +
                            "</StatusMessage>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<deleteContractRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Контракт с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

 */
}