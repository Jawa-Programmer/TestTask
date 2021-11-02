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
import ru.jawaprog.test_task.dao.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;
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
class PhoneNumbersControllerTest {

    private static final String BASE_PATH = "/phone-numbers";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PhoneNumbersService service;

    // бины дальше созданы потому что без них тест не запускается, хотя по факту они нигде не используются в тесте
    @MockBean
    private ClientsService clientsService;
    @MockBean
    private ContractsService contractsService;
    @MockBean
    private AccountsService accountsService;

    Collection<PhoneNumber> database;

    @BeforeAll
    void initial() {
        database = new HashSet<>();
        {
            PhoneNumber c = new PhoneNumber();
            c.setId(1);
            c.setNumber("+7 (800) 555-35-35");
            database.add(c);
        }
        {
            PhoneNumber c = new PhoneNumber();
            c.setId(2);
            c.setNumber("+7 (999) 888-77-66");
            database.add(c);
        }
    }

    @AfterAll
    void cleanup() {
        database.clear();
    }

    @Test
    void getPhoneNumber() throws Exception {
        {
            PhoneNumber acc = database.stream().findFirst().get();
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
    void getPhoneNumbers() throws Exception {
        Mockito.when(service.findAll()).thenReturn(database);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(database)));
    }

    @Test
    void postPhoneNumber() throws Exception {
        {
            PhoneNumber acc = database.stream().findFirst().get();

            Mockito.when(service.saveNew(any(PhoneNumber.class))).thenReturn(acc);
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("number", "+7 (888) 777-66-55")
                            .param("accountId", "1"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(acc)));
        }
        {
            Mockito.when(service.saveNew(any(PhoneNumber.class))).thenThrow(ForeignKeyException.class);
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH, 1)
                            .param("accountId", "4")
                            .param("number", "231"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ForeignKeyException.class));

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("accountId", "-1")
                            .param("number", "231"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("accountId", "1")
                            .param("number", "INL"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MissingServletRequestParameterException.class));
        }
    }

    @Test
    void putPhoneNumber() throws Exception {
        {
            PhoneNumber acc = database.stream().findFirst().get();
            Mockito.when(service.update(1, null, 1L)).thenReturn(acc);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("accountId", "1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(acc)));
        }
        {
            Mockito.when(service.update(1, null, 1L)).thenThrow(NotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("accountId", "1"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));

            Mockito.when(service.update(1, null, 2L)).thenThrow(ForeignKeyException.class);
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("accountId", "2"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ForeignKeyException.class));


            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", -55))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 23)
                            .param("accountId", "-1"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 11)
                            .param("number", "88005553535"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("accountId", "ajc"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
        }
    }

    @Test
    void deletePhoneNumber() throws Exception {
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