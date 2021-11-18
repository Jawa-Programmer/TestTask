package ru.jawaprog.test_task.web.soap.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ws.test.server.MockWebServiceClient;
import ru.jawaprog.test_task.web.rest.services.AccountsService;
import ru.jawaprog.test_task.web.rest.services.ClientsService;
import ru.jawaprog.test_task.web.rest.services.ContractsService;
import ru.jawaprog.test_task.web.rest.services.PhoneNumbersService;
import ru.jawaprog.test_task.web.soap.services.AccountsSoapService;
import ru.jawaprog.test_task.web.soap.services.ContractsSoapService;
import ru.jawaprog.test_task.web.soap.services.PhoneNumbersSoapService;
import ru.jawaprog.test_task.web.utils.Utils;

@TestConfiguration
@ComponentScan("ru.jawaprog.test_task.web.soap.endpoints")
public class TestConfig {
    @Bean
    Utils getUtils(ObjectMapper objectMapper) {
        return new Utils(objectMapper);
    }

    @Bean()
    MockWebServiceClient mockClient(ApplicationContext context) {
        return MockWebServiceClient.createClient(context);
    }


    @MockBean
    private ContractsSoapService contractsSoapService;
    @MockBean
    private AccountsSoapService accountsSoapService;
    @MockBean
    private PhoneNumbersSoapService phoneNumbersSoapService;

    // Нижележащие бины созданы потому что без них тест не запускается, хотя по факту они нигде не используются в тесте
    @MockBean
    private ClientsService clientsService;
    @MockBean
    private AccountsService accountsService;
    @MockBean
    private ContractsService contractsService;
    @MockBean
    private PhoneNumbersService phoneNumbersService;
}
