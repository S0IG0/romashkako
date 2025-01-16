package com.soigo.romashkako.handler;

import com.soigo.romashkako.dto.response.ErrorResponse;
import com.soigo.romashkako.exception.EntityNotFoundException;
import com.soigo.romashkako.exception.ValueLessThanZeroException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse
                        .builder()
                        .uri(request.getRequestURI())
                        .message("Ошибка валидации данных")
                        .details(errors)
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidateRequestParamsException(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : violations) {
            String fieldName = parseFieldName(violation.getPropertyPath().toString());
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse
                        .builder()
                        .uri(request.getRequestURI())
                        .message("Ошибка валидации параметров запроса")
                        .details(errors)
                        .build()
        );
    }

    @ExceptionHandler(ValueLessThanZeroException.class)
    public ResponseEntity<ErrorResponse> handleValueLessThanZeroException(ValueLessThanZeroException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse
                        .builder()
                        .uri(request.getRequestURI())
                        .message(ex.getMessage())
                        .details(ex.getDetails())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse
                        .builder()
                        .uri(request.getRequestURI())
                        .message(ex.getMessage())
                        .details(Map.of())
                        .build()
        );
    }

    private String parseFieldName(String string) {
        String[] split = string.split("\\.");
        return split[split.length - 1];
    }
}
