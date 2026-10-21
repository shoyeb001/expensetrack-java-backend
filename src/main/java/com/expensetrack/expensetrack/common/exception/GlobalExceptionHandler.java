package com.expensetrack.expensetrack.common.exception;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice // this annotation is used to handle exceptions globally in a Spring Boot
                      // application. It allows you to define methods that will be invoked when
                      // specific exceptions are thrown, enabling centralized exception handling
                      // across all controllers.
public class GlobalExceptionHandler {

        @ExceptionHandler // this annotation is used to specify the type of exception that the method will
                          // handle. When an exception of the specified type is thrown, the annotated
                          // method will be invoked to handle it.
        public ResponseEntity<ApiError> handelNotFound(
                        EntityNotFoundException ex,
                        HttpServletRequest request) {
                return buildError(
                                HttpStatus.NOT_FOUND,
                                "Resource not found",
                                request.getRequestURI(),
                                Map.of());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> validationErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .collect(Collectors.toMap(
                                                error -> error.getField(),
                                                error -> error.getDefaultMessage() == null
                                                                ? "Invalid value"
                                                                : error.getDefaultMessage(),
                                                (first, second) -> first));
                return buildError(
                                HttpStatus.BAD_REQUEST,
                                "Invalid request parameters",
                                request.getRequestURI(),
                                validationErrors);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleUnexpected(
                        Exception ex,
                        HttpServletRequest request) {
                return buildError(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred",
                                request.getRequestURI(),
                                Map.of());
        }

        public ResponseEntity<ApiError> buildError(
                        HttpStatus status,
                        String message,
                        String path,
                        Map<String, String> validationErrors) {
                ApiError apiError = new ApiError(
                                Instant.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                message,
                                path,
                                validationErrors);
                return new ResponseEntity<>(apiError, status);
        }

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<ApiError> handleDuplicateEmail(
                        EmailAlreadyExistsException ex,
                        HttpServletRequest request) {
                return buildError(
                                HttpStatus.CONFLICT,
                                ex.getMessage(),
                                request.getRequestURI(),
                                Map.of());
        }
}
