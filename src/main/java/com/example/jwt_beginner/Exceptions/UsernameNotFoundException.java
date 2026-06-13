package com.example.jwt_beginner.Exceptions;

import com.example.jwt_beginner.Entity.User;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message) {
        super(message);
    }

    public UsernameNotFoundException(String username, Throwable cause){
        super(username,cause);
    }
}
