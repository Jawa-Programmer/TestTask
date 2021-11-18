package ru.jawaprog.test_task.web.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.jawaprog.test_task.web.rest.services.AccountsService;
import ru.jawaprog.test_task.web.rest.services.ClientsService;
import ru.jawaprog.test_task.web.rest.services.ContractsService;
import ru.jawaprog.test_task.web.rest.services.PhoneNumbersService;
import ru.jawaprog.test_task.web.utils.Utils;

@TestConfiguration
public class TestConfig {
    @Bean
    Utils getUtils(ObjectMapper objectMapper) {
        return new Utils(objectMapper);
    }

    @MockBean
    private ClientsService clientsService;
    @MockBean
    private ContractsService contractsService;
    @MockBean
    private AccountsService accountsService;
    @MockBean
    private PhoneNumbersService phoneNumbersService;
}
