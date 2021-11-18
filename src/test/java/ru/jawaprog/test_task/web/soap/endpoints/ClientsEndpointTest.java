package ru.jawaprog.test_task.web.soap.endpoints;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;
import ru.jawaprog.test_task.configuration.WebServiceConfig;

import javax.sql.DataSource;
import javax.xml.transform.Source;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
@ContextConfiguration(classes = {TestConfig.class, WebServiceConfig.class})
class ClientsEndpointTest {

    @Autowired
    private MockWebServiceClient mockClient;

    @Autowired
    private DataSource driver;

    @BeforeAll
    void initDatabase() {
        Resource initSchema = new ClassPathResource("client-test.sql", getClass().getClassLoader());
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, driver);
    }

    @Test
    void getClients() {

        Source requestPayload = new StringSource(
                "<getClientsRequest xmlns = 'http://jawaprog.ru/test-task-mts'/>");
        Source responsePayload = new StringSource(
                "<ClientsListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                        "<client fullName='Иванов И. И.' id='1' type='INDIVIDUAL'/>" +
                        "<client fullName=\"ОАО 'Общество Гигантских растений'\" id='2' type='ENTITY'/>" +
                        "</ClientsListResponse>");
        mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));

    }

    @Test
    void getClient() {
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
                    "<getClientRequest id='99' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Клиент с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void findClientsByName() {
        {
            Source requestPayload = new StringSource(
                    "<findClientsByNameRequest fullName='Ив' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ClientsListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<client fullName='Иванов И. И.' id='1' type='INDIVIDUAL'/>" +
                            "</ClientsListResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void findClientsByPhone() {
        {
            Source requestPayload = new StringSource(
                    "<findClientsByPhoneRequest number='+7 (800) 555-35-35' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<ClientsListResponse xmlns='http://jawaprog.ru/test-task-mts'/>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void postClient() {
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
                    "<updateClientRequest id='99' type='ENTITY' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Клиент с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void deleteClient() {
        {
            Source requestPayload = new StringSource(
                    "<deleteClientRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<StatusMessage xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<message>Клиент успешно удалён.</message>" +
                            "</StatusMessage>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<deleteClientRequest id='100' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Клиент с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

}