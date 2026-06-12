package com.example.jwt_beginner.Controller;

import com.example.jwt_beginner.DTO.ErrorDTO;
import com.example.jwt_beginner.Exceptions.DuplicateProductNameException;
import com.example.jwt_beginner.Exceptions.InvalidProductException;
import com.example.jwt_beginner.Exceptions.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request){
        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .error("PRODUCT_NOT_FOUND")
                .path(request.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorDTO,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateProductNameException.class)
    public ResponseEntity<ErrorDTO> handleDuplicateProduct(DuplicateProductNameException ex, HttpServletRequest request){
        ErrorDTO error = ErrorDTO.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .error("DUPLICATE_PRODUCT_NAME")
                .path(request.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorDTO> handleInvalidProduct(InvalidProductException ex, HttpServletRequest request){
        ErrorDTO error = ErrorDTO.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .error(ex.getLocalizedMessage())
                .path(request.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error,HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ){
        List<ErrorDTO.FieldErrorDTO> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error-> new ErrorDTO.FieldErrorDTO(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value()).message("Validation failed")
                .error("VALIDATION_ERROR")
                .path(request.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();

        return new ResponseEntity<>(errorDTO,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGlobalException(Exception ex, HttpServletRequest request){
        ErrorDTO error = ErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred. Please contact support")
                .error("INTERNAL_SERVER_ERROR")
                .path(request.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
