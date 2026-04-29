package com.jane.project.common.exceptions;

import com.jane.project.common.dto.ApiErrorResponse;
import com.jane.project.common.dto.FieldErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateContactException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateContact(DuplicateContactException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .type("BUSINESS")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldErrorDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> FieldErrorDto.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .type("VALIDATION")
                .message("Некоректні дані запиту")
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .type("SYSTEM")
                .message("Сталася внутрішня помилка")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

