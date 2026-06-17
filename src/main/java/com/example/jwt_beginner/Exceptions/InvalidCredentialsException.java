package com.example.jwt_beginner.Exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String error, Throwable cause){
        super(error,cause);
    }
}
