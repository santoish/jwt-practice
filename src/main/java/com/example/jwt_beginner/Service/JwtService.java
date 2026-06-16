package com.example.jwt_beginner.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;


    public String generateToken(UserDetails userDetails){
        return createToken(userDetails.getUsername());
    }

    private String createToken(String userName){

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + expiration);
        return Jwts.builder()
                .subject(userName)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();

    }

    public String extractUsername(String token){
        return getUsernameFromToken(token);
    }

    private String getUsernameFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String tokenUsername = getUsernameFromToken(token);

        boolean usernameMatches = tokenUsername.equals(userDetails.getUsername());

        boolean tokenExpired = !isTokenExpired(token);

        return usernameMatches && tokenExpired;
    }

    private boolean isTokenExpired(String token){
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
