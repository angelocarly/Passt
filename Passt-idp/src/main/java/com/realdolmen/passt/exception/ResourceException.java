package com.realdolmen.passt.exception;

import org.springframework.http.HttpStatus;


/**
 * The exception used to return a status and a message to the calling system.
 * @author norrisshelton
 */
@SuppressWarnings("ClassWithoutNoArgConstructor")
public class ResourceException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * Gets the HTTP status code to be returned to the calling system.
     * @return http status code.  Defaults to HttpStatus.INTERNAL_SERVER_ERROR (500).
     * @see HttpStatus
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Constructs a new runtime exception with the specified HttpStatus code and detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     * @param httpStatus the http status.  The detail message is saved for later retrieval by the {@link
     *                   #getHttpStatus()} method.
     * @param message    the detail message. The detail message is saved for later retrieval by the {@link
     *                   #getMessage()} method.
     * @see HttpStatus
     */
    public ResourceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}