package com.example.jwt_beginner.Exceptions;

public class DuplicateProductNameException extends RuntimeException {
    public DuplicateProductNameException(String message) {
        super(message);
    }

    public DuplicateProductNameException(String productName, Throwable cause){
        super(productName, cause);
    }
}
