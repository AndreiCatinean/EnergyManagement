package org.example.userbackend.controllers.handlers.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final String USER_NOT_FOUND_EXCEPTION = "User not found";


    public UserNotFoundException() {
        super(USER_NOT_FOUND_EXCEPTION);
    }
}
