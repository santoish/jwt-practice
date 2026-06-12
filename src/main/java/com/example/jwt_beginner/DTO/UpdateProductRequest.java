package com.example.jwt_beginner.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

    @NotBlank
    private String name;
    @NotNull
    private BigDecimal price;
    @Max(value = 100, message = "Quantity Cannot exceed 100")
    @Min(value = 0,message = "Quantity cannot be negative")
    private Integer quantity;
}
