package com.example.jwt_beginner.JWT;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class GenerateSecretKey {
    public static void main(String[] args) {
        byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(key);

        System.out.println("JWT Key : ");
        System.out.println(base64Key);
    }
}
