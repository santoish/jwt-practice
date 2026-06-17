package com.example.jwt_beginner.Controller;

import com.example.jwt_beginner.DTO.LoginRequest;
import com.example.jwt_beginner.DTO.LoginResponse;
import com.example.jwt_beginner.DTO.RegisterRequest;
import com.example.jwt_beginner.DTO.RegisterResponse;
import com.example.jwt_beginner.Service.AuthService;
import com.example.jwt_beginner.Service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AuthService authService;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
            ) {
        log.info("Register endpoint called for user {} : ", request.getUsername());
        RegisterResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
            ){
        log.info("Login Endpoint called for user : {}", request.getUsername());

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
