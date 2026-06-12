package com.example.jwt_beginner.Exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String productName, Throwable cause){
        super(productName, cause);
    }
}
