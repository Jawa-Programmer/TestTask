package ru.jawaprog.test_task.web.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.jawaprog.test_task.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.exceptions.InvalidParamsException;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.TestConfig;
import ru.jawaprog.test_task.web.rest.entities.Account;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;
import ru.jawaprog.test_task.web.rest.entities.PhoneNumber;

import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
@ContextConfiguration(classes = TestConfig.class)
class PhoneNumbersControllerTest {

    private static final String BASE_PATH = "/phone-numbers";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource driver;

    @BeforeEach
    void initDatabase() {
        Resource initSchema = new ClassPathResource("rest/test-data.sql", getClass().getClassLoader());
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, driver);
    }

    private static final Account account1, account2, account3;

    static {
        Client client2 = new Client(2L, "ОАО 'Общество Гигантских растений'", Client.ClientType.ENTITY);
        Client client1 = new Client(1L, "Иванов И. И.", Client.ClientType.INDIVIDUAL);
        Contract contract1 = new Contract(1L, 123L, client2);
        Contract contract2 = new Contract(2L, 321L, client1);
        account1 = new Account(1L, 42L, contract1);
        account2 = new Account(2L, 33L, contract1);
        account3 = new Account(3L, 21L, contract2);
    }


    @Test
    void getPhoneNumber() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            new PhoneNumber(1L, "+7 (800) 555-35-35", account2)
                    )));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", 100))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }

    @Test
    void getPhoneNumbers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        List.of(
                                new PhoneNumber(1L, "+7 (800) 555-35-35", account2),
                                new PhoneNumber(2L, "+7 (911) 111-22-33", account1),
                                new PhoneNumber(3L, "+7 (123) 456-78-90", account2)
                        )
                )));
    }

    @Test
    void postPhoneNumber() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("number", "+7 (888) 777-66-55")
                            .param("accountId", "3"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new PhoneNumber(4L, "+7 (888) 777-66-55", account3))));
        }
        {
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
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("accountId", "3"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            new PhoneNumber(1L, "+7 (800) 555-35-35", account3)
                    )));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 100)
                            .param("accountId", "1"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 100)
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
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", 1))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", 100))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }


}