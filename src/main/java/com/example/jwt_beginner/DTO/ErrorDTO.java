package com.example.jwt_beginner.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {

    private int status;
    private String message;
    private String error;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timeStamp;
    private String path;
    private List<FieldErrorDTO> fieldErrors;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldErrorDTO {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}
