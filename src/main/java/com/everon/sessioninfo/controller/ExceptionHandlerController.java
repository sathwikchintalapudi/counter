package com.everon.sessioninfo.controller;

import com.everon.sessioninfo.exceptions.SessionStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.everon.sessioninfo.domain.Error;

import static com.everon.sessioninfo.domain.SessionStoreConstants.INTERNAL_TECHNICAL_ERROR;

/**
 * @author Sathwik on 21/june/2020
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /**
     * Handles SessionStoreException and converts into error object.
     *
     * @param exception, generic SessionStoreException with user specified error code and description
     * @return error with code and description
     */
    @ExceptionHandler({SessionStoreException.class})
    public ResponseEntity handleSessionStoreExceptions(SessionStoreException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles Exception and converts into error.
     *
     * @param exception, Exception
     * @return error with code and description
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity handleGeneralExceptions(Exception exception) {
        log.error("Unexpected technical exception occurred", exception);
        return new ResponseEntity<>(formError("Unexpected technical error occurred", INTERNAL_TECHNICAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Error formError(String errorDescription, String errorCode) {
        Error error = new Error();
        error.setErrorDescription(errorDescription);
        error.setErrorCode(errorCode);
        return error;
    }

}
