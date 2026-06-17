package com.example.jwt_beginner.Service;


import com.example.jwt_beginner.DTO.LoginRequest;
import com.example.jwt_beginner.DTO.LoginResponse;
import com.example.jwt_beginner.DTO.RegisterRequest;
import com.example.jwt_beginner.DTO.RegisterResponse;
import com.example.jwt_beginner.Entity.Role;
import com.example.jwt_beginner.Entity.User;
import com.example.jwt_beginner.Exceptions.InvalidCredentialsException;
import com.example.jwt_beginner.Repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    public RegisterResponse register(RegisterRequest request){
        log.info("Registration attempt for user : {}",request.getUsername());

        if(!request.getPassword().equals(request.getConfirmPassword())){
            log.warn("Passwords dont match for user : {}", request.getUsername());
            throw new InvalidCredentialsException("Password do not Match");
        }

        if(userRepository.existsByUsername(request.getUsername())){
            log.warn("Username is already exists : {}",request.getUsername());
            throw new InvalidCredentialsException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            log.warn("Email is already exists : {}",request.getEmail());
            throw new InvalidCredentialsException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(
                request.getPassword()
        ));
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);

        log.info("User registered Successfully : {}", savedUser.getUsername());

        return RegisterResponse.builder().
                id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .message("User Registered Successfully")
                .success(true)
                .build();

    }

    public LoginResponse login(LoginRequest request){

        try{
            Authentication authenticated = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticated.getName());

            String token = jwtService.generateToken(userDetails);
            long expiresIn = 86400;

            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .expiresIn(expiresIn)
                    .username(userDetails.getUsername())
                    .build();

            log.info("User {} logged in successfully",request.getUsername());

            return response;
        }catch (BadCredentialsException e){
            log.error("Invalid credentials for login");
            throw new BadCredentialsException("Invalid Username or Password");
        }

        catch (Exception e){
            log.error("Login Failed for user : {} ",request.getUsername());
            throw new InvalidCredentialsException("Login Failed");
        }
    }
}
