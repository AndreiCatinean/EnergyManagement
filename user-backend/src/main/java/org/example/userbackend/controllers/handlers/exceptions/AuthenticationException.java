package org.example.userbackend.controllers.handlers.exceptions;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {


    private static final String WRONG_CREDENTIALS_EXCEPTION = "Wrong Credentials";


    public AuthenticationException() {
        super(WRONG_CREDENTIALS_EXCEPTION);
    }
}
