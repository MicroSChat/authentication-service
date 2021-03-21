package com.microschat.authenticationservice.authentication;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String errorMessage) {
        super(errorMessage);
    }
}
