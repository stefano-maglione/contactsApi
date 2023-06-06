package com.maglione.contactsapi.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceUniqueConstraintException extends RuntimeException {

    public ResourceUniqueConstraintException() {
    }

    public ResourceUniqueConstraintException(String message) {
        super(message);
    }

    public ResourceUniqueConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
