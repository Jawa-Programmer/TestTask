package ru.jawaprog.test_task.web.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.jawaprog.test_task.web.rest.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.exceptions.InvalidParamsException;
import ru.jawaprog.test_task.web.rest.services.AccountsService;
import ru.jawaprog.test_task.web.rest.services.ClientsService;
import ru.jawaprog.test_task.web.rest.services.ContractsService;
import ru.jawaprog.test_task.web.rest.services.PhoneNumbersService;

import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
class ClientsControllerTest {


    private static final String BASE_PATH = "/clients";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClientsService service;

    // бины дальше созданы потому что без них тест не запускается, хотя по факту они нигде не используются в тесте
    @MockBean
    private AccountsService accountsService;
    @MockBean
    private ContractsService contractsService;
    @MockBean
    private PhoneNumbersService phoneNumbersService;

    Collection<Client> database;

    @BeforeAll
    void initial() {
        database = new HashSet<>();
        {
            Client cl = new Client();
            cl.setFullName("Зубенко Михаил Петрович");
            cl.setId(1);
            cl.setType(Client.ClientType.INDIVIDUAL);
            database.add(cl);
        }
        {
            Client cl = new Client();
            cl.setFullName("ИП Иванова");
            cl.setId(2);
            cl.setType(Client.ClientType.ENTITY);
            database.add(cl);
        }
    }

    @AfterAll
    void cleanup() {
        database.clear();
    }

    @Test
    void getClients() throws Exception {
        Mockito.when(service.findAll()).thenReturn(database);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(database)));
    }


    @Test
    void getClient() throws Exception {
        {
            Client acc = database.stream().findFirst().get();

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
    void getClientsContracts() throws Exception {
        {
            Collection<Contract> database = new HashSet<>();

            Mockito.when(service.getClientsContracts(1)).thenReturn(database);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{1}/contracts", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(database)));
        }
        {
            Mockito.when(service.getClientsContracts(1)).thenThrow(NotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/contracts", 1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/contracts", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }

    @Test
    void findClients() throws Exception {
        String name = "Zubenco"; // mockMvc не может в кириллицу. приходится обходить недостаток
        Mockito.when(service.findByName(name)).thenReturn(database);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/findByName/{name}", name))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(database)));
    }

    @Test
    void findClientsByNumber() throws Exception {

        Mockito.when(service.findByPhoneNumber("+7 (800) 555-35-35")).thenReturn(database);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/findByPhoneNumber/{number}", "+7 (800) 555-35-35"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(database)));
    }


    @Test
    void postClient() throws Exception {
        {
            Client acc = database.stream().findFirst().get();

            Mockito.when(service.saveNew(any(Client.class))).thenReturn(acc);
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("fullName", "Зубенко Михаил Петрович")
                            .param("type", "INDIVIDUAL"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(acc)));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("fullName", "Зу")
                            .param("type", "INDIVIDUAL"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("fullName", "Зубенко Михаил Петрович")
                            .param("type", "INDEVIDUAL"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MissingServletRequestParameterException.class));
        }
    }

    @Test
    void updateClient() throws Exception {
        {
            Client acc = database.stream().findFirst().get();
            Mockito.when(service.update(1, null, Client.ClientType.INDIVIDUAL)).thenReturn(acc);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("type", "INDIVIDUAL"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(acc)));
        }
        {
            Mockito.when(service.update(1, null, Client.ClientType.ENTITY)).thenThrow(NotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("type", "ENTITY"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("fullName", "Zu"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("type", "INDEVIDUAL"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
        }
    }

    @Test
    void deleteClient() throws Exception {
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