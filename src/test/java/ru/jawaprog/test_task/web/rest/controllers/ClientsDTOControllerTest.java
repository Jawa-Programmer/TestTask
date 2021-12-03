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
import ru.jawaprog.test_task.exceptions.InvalidParamsException;
import ru.jawaprog.test_task.exceptions.NotFoundException;
import ru.jawaprog.test_task.web.TestConfig;
import ru.jawaprog.test_task.web.rest.entities.Client;
import ru.jawaprog.test_task.web.rest.entities.Contract;

import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
@ContextConfiguration(classes = TestConfig.class)
class ClientsDTOControllerTest {

    private static final String BASE_PATH = "/clients";

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
    void getClients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(List.of(
                                new Client(1L, "Иванов И. И.", Client.ClientType.INDIVIDUAL),
                                new Client(2L, "ОАО 'Общество Гигантских растений'", Client.ClientType.ENTITY)
                        ))));

    }


    @Test
    void getClient() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(
                            objectMapper.writeValueAsString(
                                    new Client(1L, "Иванов И. И.", Client.ClientType.INDIVIDUAL))));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", 3))
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
            Client c = new Client(1L, "Иванов И. И.", Client.ClientType.INDIVIDUAL);
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{1}/contracts", 1))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(
                            objectMapper.writeValueAsString(
                                    List.of(new Contract(2L, 321L, c),
                                            new Contract(3L, 666L, c))
                            )
                    ));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/contracts", 100))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}/contracts", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }

    @Test
    void findClients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/findByName/{name}", "ван"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        List.of(new Client(1L, "Иванов И. И.", Client.ClientType.INDIVIDUAL))
                )));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/findByName/{name}", "общество"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        List.of(new Client(2L, "ОАО 'Общество Гигантских растений'", Client.ClientType.ENTITY))
                )));
    }

    @Test
    void findClientsByNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/findByPhoneNumber/{number}", "+7 (800) 555-35-35"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        List.of(new Client(2L, "ОАО 'Общество Гигантских растений'", Client.ClientType.ENTITY))
                )));
    }

    @Test
    void postClient() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("fullName", "Зубенко Михаил Петрович")
                            .param("type", "INDIVIDUAL"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            new Client(3L, "Зубенко Михаил Петрович", Client.ClientType.INDIVIDUAL)
                    )));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("fullName", "Зу")
                            .param("type", "INDIVIDUAL"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
                            .param("fullName", "Зубенко Михаил Петрович")
                            .param("type", "Мафиозник"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MissingServletRequestParameterException.class));
        }
    }

    @Test
    void updateClient() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("type", "ENTITY"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                            new Client(1L, "Иванов И. И.", Client.ClientType.ENTITY))));
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 100)
                            .param("type", "ENTITY"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));

            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("fullName", "Zu"))

                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", 1)
                            .param("type", "МАФЕОЗНИКъ"))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentTypeMismatchException.class));
        }
    }

    @Test
    void deleteClient() throws Exception {
        {
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", 1))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
        {
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", 1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", -1))
                    .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(InvalidParamsException.class));
        }
    }

}