package com.example.jwt_beginner.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private String message;
    private boolean success;
}
