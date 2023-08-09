package com.hajela.customerservice.exceptions;

import com.hajela.customerservice.domain.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = CustomerNotFound.class)
    protected ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFound exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("Customer not found")
                .message(exception.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation error")
                .errors(errorList)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
