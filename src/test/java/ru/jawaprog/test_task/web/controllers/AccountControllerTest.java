package ru.jawaprog.test_task.web.controllers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.jawaprog.test_task.dao.exceptions.ForeignKeyException;
import ru.jawaprog.test_task.dao.exceptions.NotFoundException;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.web.entities.Account;
import ru.jawaprog.test_task.web.entities.Contract;
import ru.jawaprog.test_task.web.entities.PhoneNumber;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerTest {

    private AccountController subject;
    private Collection<Account> database;
    @Mock
    private AccountsService service;

    @BeforeAll
    void setUp() {
        initMocks(this);
        subject = new AccountController(service);
        database = new HashSet<>();
    }

    @AfterAll
    void tearDown() {
        database.clear();
        database = null;
    }

    @Test
    void getAccount() {
        {
            Account acc = new Account();
            acc.setId(1);
            acc.setNumber(89);
            given(service.get(1)).willReturn(acc);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", "http://localhost:8080/accoints/1"));
            ResponseEntity<Account> resp = subject.getAccount(req, 1);
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals(acc, resp.getBody());
        }
        {
            given(service.get(anyLong())).willThrow(NotFoundException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", "http://localhost:8080/accoints/1"));
            assertThrows(NotFoundException.class, () -> subject.getAccount(req, 1));
        }
    }

    @Test
    void getAccounts() {
        given(service.findAll()).willReturn(database);
        WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", "http://localhost:8080/accoints/"));
        ResponseEntity<Collection<Account>> resp = subject.getAccounts(req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(database, resp.getBody());
    }

    @Test
    void getPhones() {
        {
            Collection<PhoneNumber> phones = new LinkedList<>();
            given(service.getAccountsPhones(1)).willReturn(phones);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", "http://localhost:8080/accoints/1/phones"));
            ResponseEntity<Collection<PhoneNumber>> resp = subject.getAccountPhones(req, 1);
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals(phones, resp.getBody());
        }
        {
            given(service.getAccountsPhones(anyLong())).willThrow(NotFoundException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", "http://localhost:8080/accoints/1/phones"));
            assertThrows(NotFoundException.class, () -> subject.getAccountPhones(req, 1));
        }
    }

    @Test
    void postAccount() {
        {
            Account acc = new Account();
            acc.setNumber(123);
            acc.setContract(new Contract());
            acc.getContract().setId(1);
            given(service.saveNew(any(Account.class))).willReturn(acc);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("POST", "http://localhost:8080/accoints/"));
            ResponseEntity<Account> resp = subject.postAccount(req, 123, 1);
            assertEquals(HttpStatus.CREATED, resp.getStatusCode());
            assertEquals(acc, resp.getBody());
        }
        {
            Account acc = new Account();
            acc.setNumber(123);
            acc.setContract(new Contract());
            acc.getContract().setId(1);
            given(service.saveNew(any(Account.class))).willThrow(ForeignKeyException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("POST", "http://localhost:8080/accoints/"));
            assertThrows(ForeignKeyException.class, () -> subject.postAccount(req, 123, 1));
        }
    }

    @Test
    void putAccount() {
        Account acc = new Account();
        acc.setId(2);
        acc.setNumber(123);
        acc.setContract(new Contract());
        acc.getContract().setId(1);
        {
            given(service.update(1, 123, 1L)).willReturn(acc);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("PUT", "http://localhost:8080/accoints/2"));
            ResponseEntity<Account> resp = subject.putAccount(req, 1, 123, 1L);
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals(acc, resp.getBody());
        }
        {
            given(service.update(2, 123, 1L)).willThrow(ForeignKeyException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("PUT", "http://localhost:8080/accoints/2"));
            assertThrows(ForeignKeyException.class, () -> subject.putAccount(req, 2, 123, 1L));
        }
        {
            given(service.update(4, 123, 1L)).willThrow(NotFoundException.class);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("PUT", "http://localhost:8080/accoints/2"));
            assertThrows(NotFoundException.class, () -> subject.putAccount(req, 4, 123, 1L));
        }
    }

    @Test
    void deleteAccount() {
        {
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("DELETE", "http://localhost:8080/accoints/1"));
            ResponseEntity resp = subject.deleteAccount(req, 1);
            assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        }
        {
            doThrow(NotFoundException.class).when(service).delete(1);
            WebRequest req = new ServletWebRequest(new MockHttpServletRequest("DELETE", "http://localhost:8080/accoints/1"));
            assertThrows(NotFoundException.class, () -> subject.deleteAccount(req, 1));
        }
    }
}