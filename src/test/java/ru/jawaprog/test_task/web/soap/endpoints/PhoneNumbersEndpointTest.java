package ru.jawaprog.test_task.web.soap.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;
import ru.jawaprog.test_task.configuration.WebServiceConfig;
import ru.jawaprog.test_task.web.TestConfig;

import javax.sql.DataSource;
import javax.xml.transform.Source;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
@ContextConfiguration(classes = {TestConfig.class, WebServiceConfig.class})
class PhoneNumbersEndpointTest {

    @Autowired
    private MockWebServiceClient mockClient;

    @Autowired
    private DataSource driver;

    @BeforeEach
    void initDatabase() {
        Resource initSchema = new ClassPathResource("soap/phone-test.sql", getClass().getClassLoader());
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, driver);
    }

    @Test
    void getPhoneNumbers() {

        Source requestPayload = new StringSource(
                "<getPhoneNumbersRequest xmlns = 'http://jawaprog.ru/test-task-mts'/>");
        Source responsePayload = new StringSource(
                "<PhoneNumbersListResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                        "<phoneNumber id='1' accountId='2' number='+7 (912) 555-33-22'/>" +
                        "<phoneNumber id='2' accountId='1' number='+7 (800) 555-35-35'/>" +
                        "</PhoneNumbersListResponse>");
        mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    void getPhoneNumber() {

        {
            Source requestPayload = new StringSource(
                    "<getPhoneNumberRequest id='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='1' accountId='2' number='+7 (912) 555-33-22'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<getPhoneNumberRequest id='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='2' accountId='1' number='+7 (800) 555-35-35'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<getPhoneNumberRequest id='3' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Номер телефона с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void postPhoneNumber() {

        {
            Source requestPayload = new StringSource(
                    "<postPhoneNumberRequest accountId='1' number='+7 (912) 555-33-22' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='3' accountId='1' number='+7 (912) 555-33-22'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<postPhoneNumberRequest accountId='3' number='+7 (911) 222-33-44' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Счёт с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void updatePhoneNumber() {

        {
            Source requestPayload = new StringSource(
                    "<updatePhoneNumberRequest id='1' number='+7 (911) 222-33-44' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='1' accountId='2' number='+7 (911) 222-33-44'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updatePhoneNumberRequest id='1' accountId='1' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<PhoneNumberResponse xmlns='http://jawaprog.ru/test-task-mts'>" +
                            "<phoneNumber id='1' accountId='1' number='+7 (911) 222-33-44'/>" +
                            "</PhoneNumberResponse>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updatePhoneNumberRequest id='3' accountId='2' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Номер телефона с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
        {
            Source requestPayload = new StringSource(
                    "<updatePhoneNumberRequest id='1' accountId='3' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Ошибка внешнего ключа. Счёт с переданным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

    @Test
    void deletePhoneNumber() {

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
                    "<deletePhoneNumberRequest id='3' xmlns = 'http://jawaprog.ru/test-task-mts'/>");
            Source responsePayload = new StringSource(
                    "<SOAP-ENV:Fault xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>" +
                            "<faultcode>SOAP-ENV:Client</faultcode>" +
                            "<faultstring xml:lang='en'>Номер телефона с данным id не найден.</faultstring>" +
                            "</SOAP-ENV:Fault>");
            mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(ResponseMatchers.payload(responsePayload));
        }
    }

}