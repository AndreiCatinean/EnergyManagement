package org.example.userbackend.controllers.handlers.exceptions;

import lombok.Getter;

@Getter
public class UsernameTakenException extends RuntimeException {

    private static final String USERNAME_TAKEN_EXCEPTION = "Username taken";


    public UsernameTakenException() {
        super(USERNAME_TAKEN_EXCEPTION);
    }

}
