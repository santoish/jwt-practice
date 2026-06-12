package com.example.jwt_beginner.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank
    @Size(min = 2,max = 255)
    private String name;
    @NotNull
    private BigDecimal price;
    @Max(value = 100)
    private Integer quantity;
}
