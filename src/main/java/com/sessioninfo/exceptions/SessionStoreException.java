package com.sessioninfo.exceptions;

import com.sessioninfo.domain.Error;

/**
 * @author Sathwik on 21/june/2020
 */
public class SessionStoreException extends RuntimeException {

    private Error error = new Error();

    public SessionStoreException(String message, String errorCode) {
        super(message);
        error.setErrorCode(errorCode);
        error.setErrorDescription(message);
    }

    public Error getError() {
        return this.error;
    }

}
