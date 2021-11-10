package ru.jawaprog.test_task.web.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.web.rest.exceptions.InvalidParamsException;
import ru.jawaprog.test_task.web.rest.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.rest.services.AccountsService;
import ru.jawaprog.test_task.web.rest.services.ClientsService;
import ru.jawaprog.test_task.web.rest.services.ContractsService;
import ru.jawaprog.test_task.web.rest.services.PhoneNumbersService;
import ru.jawaprog.test_task.web.utils.Utils;

import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
class ContractsControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        Utils getUtils(ObjectMapper objectMapper) {
            return new Utils(objectMapper);
        }
    }

    private static final String BASE_PATH = "/contracts";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ContractsService service;

    // бины дальше созданы потому что без них тест не запускается, хотя по факту они нигде не используются в тесте
    @MockBean
    private ClientsService clientsService;
    @MockBean
    private AccountsService accountsService;
    @MockBean
    private PhoneNumbersService phoneNumbersService;

    Collection<Contract> database;

    @BeforeAll
    void initial() {
        database = new HashSet<>();
        Client cl = new Client();
        cl.setFullName("ОАО \"Общество огромных растений\"");
        cl.setId(1);
        cl.setType(Client.ClientType.ENTITY);
        {
            Contract c = new Contract();
            c.setId(1);
            c.setNumber(8800);
            c.setClient(cl);
            database.add(c);
        }
        {
            Contract c = new Contract();
            c.setId(2);
            c.setNumber(84112);
            c.setClient(cl);
            database.add(c);
        }
    }

    @AfterAll
    void cleanup() {
        database.clear();
    }

    @Test
    void getContract() throws Exception {
        {
            Contract acc = database.stream().findFirst().get();
            Mockito.when(service.get(1)).thenReturn(acc);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(acc)));
        }
        {
            Mockito.when(service.get(anyLong())).thenThrow(NotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", 1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }

    @Test
    void getContractAccounts() throws Exception {
        {
            Collection<Account> database = new HashSet<>();

            Mockito.when(service.getContractsAccounts(1)).thenReturn(database);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{1}/accounts", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(database)));
        }
        {
            Mockito.when(service.getContractsAccounts(1)).thenThrow(NotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/accounts", 1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/accounts", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }

    @Test
    void getContracts() throws Exception {
        Mockito.when(service.findAll()).thenReturn(database);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(database)));
    }

    @Test
    void postContract() throws Exception {
        {
            Contract acc = database.stream().findFirst().get();

            Mockito.when(service.saveNew(any(Contract.class))).thenReturn(acc);
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("number", "880332")
                            .param("clientId", "1"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(acc)));
        }
        {
            Mockito.when(service.saveNew(any(Contract.class))).thenThrow(ForeignKeyException.class);
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH, 1)
                            .param("clientId", "1")
                            .param("number", "231"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ForeignKeyException.class));

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("clientId", "-1")
                            .param("number", "231"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("clientId", "fderwe")
                            .param("type", "INL"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MissingServletRequestParameterException.class));
        }
    }

    @Test
    void putContract() throws Exception {
        {
            Contract acc = database.stream().findFirst().get();
            Mockito.when(service.update(1, null, 1L)).thenReturn(acc);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("clientId", "1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(acc)));
        }
        {
            Mockito.when(service.update(1, null, 1L)).thenThrow(NotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("clientId", "1"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));

            Mockito.when(service.update(1, null, 2L)).thenThrow(ForeignKeyException.class);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("clientId", "2"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ForeignKeyException.class));


            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("clientId", "-1"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("number", "ajc"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
        }
    }

    @Test
    void deleteContract() throws Exception {
        {
            Mockito.doNothing().when(service).delete(1);
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", 1))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
        {
            Mockito.doThrow(NotFoundException.class).when(service).delete(1);
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", 1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }
}