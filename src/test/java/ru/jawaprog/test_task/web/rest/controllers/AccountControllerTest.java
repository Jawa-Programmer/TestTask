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
class AccountControllerTest {

    private static final String BASE_PATH = "/accounts";
    private static final Contract contract1;
    private static final Contract contract2;
    private static final Contract contract3;

    static {
        Client client2 = new Client(2L, "ОАО 'Общество Гигантских растений'", Client.ClientType.ENTITY);
        Client client1 = new Client(1L, "Иванов И. И.", Client.ClientType.INDIVIDUAL);
        contract1 = new Contract(1L, 123L, client2);
        contract2 = new Contract(2L, 321L, client1);
        contract3 = new Contract(3L, 666L, client1);
    }

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

    @Test
    void getAccount() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            new Account(1L, 42L, contract1)
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
    void getAccountPhones() throws Exception {
        {
            Account a = new Account(1L, 42L, contract1);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{1}/phones", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            List.of(new PhoneNumber(2L, "+7 (911) 111-22-33", a))
                    )));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/phones", 100))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/phones", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }

    @Test
    void getAccounts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        List.of(
                                new Account(1L, 42L, contract1),
                                new Account(2L, 33L, contract1),
                                new Account(3L, 21L, contract2)
                        )
                )));
    }

    @Test
    void postAccount() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("number", "880332")
                            .param("contractId", "1"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            new Account(4L, 880332L, contract1)
                    )));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH, 1)
                            .param("contractId", "100")
                            .param("number", "231"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ForeignKeyException.class));

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("contractId", "-1")
                            .param("number", "231"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("contractId", "fderwe")
                            .param("number", "INL"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MissingServletRequestParameterException.class));
        }
    }

    @Test
    void putAccount() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("contractId", "3"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            new Account(1L, 42L, contract3)
                    )));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 100)
                            .param("contractId", "1"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("contractId", "200"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ForeignKeyException.class));


            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", -55)
                            .param("number", "123"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 23)
                            .param("contractId", "-1"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("number", "ajc"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
        }
    }

    @Test
    void deleteAccount() throws Exception {
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