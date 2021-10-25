package ru.jawaprog.test_task.web.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Client;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.exceptions.InvalidParamsException;

import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientsControllerTest {

    private static final String BASE_PATH = "http://localhost:8080/clients/";

    ClientsController subject;

    @Mock
    ClientsService service;

    @BeforeAll
    void setUp() {
        initMocks(this);
        subject = new ClientsController(service);
    }

    @Test
    void getClient() {
        {
            Client acc = new Client();
            acc.setId(1);
            acc.setFullName("Зубенко Михаил Петрович");
            acc.setType(Client.ClientType.INDIVIDUAL);
            given(service.get(1)).willReturn(acc);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + 1));
            ResponseEntity<Client> resp = subject.getClient(req, 1);
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals(acc, resp.getBody());
        }
        {
            given(service.get(anyLong())).willThrow(NotFoundException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + 1));
            assertThrows(NotFoundException.class, () -> subject.getClient(req, 1));
        }
        {
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + (-1)));
            assertThrows(InvalidParamsException.class, () -> subject.getClient(req, -1));
        }
    }

    @Test
    void getClientsContracts() {
        {
            given(service.getClientsContracts(anyLong())).willThrow(NotFoundException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + 1));
            assertThrows(NotFoundException.class, () -> subject.getClientsContracts(req, 1));
        }
        {
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + (-1)));
            assertThrows(InvalidParamsException.class, () -> subject.getClientsContracts(req, -1));
        }
    }

    @Test
    void findClients() {
        Collection<Client> database = new HashSet<>();

        given(service.findByName("Зубенко")).willReturn(database);
        WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH+"findByName/Зубенко"));
        ResponseEntity<Collection<Client>> resp = subject.findClients(req, "Зубенко");
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(database, resp.getBody());
    }

    @Test
    void findClientsByNumber() {
        Collection<Client> database = new HashSet<>();

        given(service.findByPhoneNumber("+7 (800) 555-35-35")).willReturn(database);
        WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH+"findByPhone/+7 (800) 555-35-35"));
        ResponseEntity<Collection<Client>> resp = subject.findClientsByNumber(req, "+7 (800) 555-35-35");
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(database, resp.getBody());
    }

    @Test
    void getClients() {
        Collection<Client> database = new HashSet<>();

        given(service.findAll()).willReturn(database);
        WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH));
        ResponseEntity<Collection<Client>> resp = subject.getClients(req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(database, resp.getBody());
    }

    @Test
    void postClient() {
        Client acc = new Client();
        acc.setFullName("Зубенко Михаил Петрович");
        acc.setType(Client.ClientType.INDIVIDUAL);
        acc.setId(1);
        given(service.saveNew(any(Client.class))).willReturn(acc);
        WebRequest req = new ServletWebRequest(new MockHttpServletRequest("POST", BASE_PATH));
        ResponseEntity<Client> resp = subject.postClient(req, "Зубенко Михаил Петрович", Client.ClientType.INDIVIDUAL);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(acc, resp.getBody());
    }

    @Test
    void updateClient() {
        {
            Client acc = new Client();
            acc.setId(1);
            acc.setFullName("Зубенко Михаил Петрович");
            acc.setType(Client.ClientType.INDIVIDUAL);
            given(service.update(1, null, null)).willReturn(acc);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + 1));
            ResponseEntity<Client> resp = subject.updateClient(req, 1, null, null);
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals(acc, resp.getBody());
        }
        {
            given(service.update(1, null, null)).willThrow(NotFoundException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + 1));
            assertThrows(NotFoundException.class, () -> subject.updateClient(req, 1, null, null));
        }
        {
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + (-1)));
            assertThrows(InvalidParamsException.class, () -> subject.updateClient(req, -1, null, null));
        }
    }

    @Test
    void deleteClient() {
        {
            doThrow(NotFoundException.class).when(service).delete(1);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + 1));
            assertThrows(NotFoundException.class, () -> subject.deleteClient(req, 1));
        }
        {
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", BASE_PATH + (-1)));
            assertThrows(InvalidParamsException.class, () -> subject.deleteClient(req, -1));
        }
    }
}