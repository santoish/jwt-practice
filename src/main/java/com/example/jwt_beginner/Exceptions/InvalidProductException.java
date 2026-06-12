package com.example.jwt_beginner.Exceptions;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException(String message) {
        super(message);
    }

    public InvalidProductException(String productName, Throwable cause){
        super(productName, cause);
    }
}
