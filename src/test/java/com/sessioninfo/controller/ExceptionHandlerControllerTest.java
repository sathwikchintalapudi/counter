package com.sessioninfo.controller;

import com.sessioninfo.exceptions.SessionStoreException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sessioninfo.domain.Error;

import static org.junit.Assert.assertEquals;

public class ExceptionHandlerControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ExceptionHandlerController exceptionHandlerController;

    @Before
    public void initiateTest() {
        exceptionHandlerController = new ExceptionHandlerController();
    }

    @Test
    public void handleSessionStoreExceptions() {
        ResponseEntity resp = exceptionHandlerController.handleSessionStoreExceptions(formSessionStoreException());
        assertEquals("Exception message is not as expected", ((Error) resp.getBody()).getErrorDescription(), "Exception-Test");
        assertEquals("Exception code is not as expected", ((Error) resp.getBody()).getErrorCode(), "333");
        assertEquals("Status code is as expected", resp.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void handleGeneralExceptions() {
        ResponseEntity resp = exceptionHandlerController.handleGeneralExceptions(formSessionStoreException());
        assertEquals("Exception message is not as expected", ((Error) resp.getBody()).getErrorDescription(), "Unexpected technical error occurred");
        assertEquals("Exception code is not as expected", ((Error) resp.getBody()).getErrorCode(), "1700");
        assertEquals("Status code is as expected", resp.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private SessionStoreException formSessionStoreException() {
        return new SessionStoreException("Exception-Test", "333");
    }
}
