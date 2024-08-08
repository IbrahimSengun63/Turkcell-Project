package com.turkcell.staj.core.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.util.Map;
import java.util.NoSuchElementException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusinessExceptionDetails handleBusinessException(BusinessException businessException, HttpServletRequest request) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail(businessException.getMessage());
        businessExceptionDetails.setType(request.getRequestURI());
        return businessExceptionDetails;
    }

    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessExceptionDetails handleNoSuchElementException(NoSuchElementException exception, HttpServletRequest request) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail(exception.getMessage());
        businessExceptionDetails.setStatus("500");
        businessExceptionDetails.setType(request.getRequestURI());
        return businessExceptionDetails;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusinessExceptionDetails handleParseErrors(HttpServletRequest request) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail("Parse Error");
        businessExceptionDetails.setStatus("400");
        businessExceptionDetails.setType(request.getRequestURI());
        return businessExceptionDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDetails handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Create a new instance of ValidationExceptionDetails to encapsulate validation errors
        ValidationExceptionDetails validationExceptionDetails = new ValidationExceptionDetails();
        // Set the message for the validation exception
        validationExceptionDetails.setDetail("VALIDATION.EXCEPTION");
        validationExceptionDetails.setType(request.getRequestURI());
        // Extract field errors from the exception, convert them into a map, and store them in validationExceptionDetails
        Map<String, String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        // Set the validation errors
        validationExceptionDetails.setValidationErrors(validationErrors);
        // Return the instance containing the validation errors
        return validationExceptionDetails;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDetails handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpServletRequest request) {
        // Create and initialize ValidationExceptionDetails
        ValidationExceptionDetails validationExceptionDetails = new ValidationExceptionDetails();
        validationExceptionDetails.setDetail("Validation failure");
        validationExceptionDetails.setType(request.getRequestURI());

        // Process validation errors
        Map<String, String> validationErrors = ex.getAllValidationResults().stream()
                .flatMap(validationResult -> validationResult.getResolvableErrors().stream())
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        // Set and return validation errors
        validationExceptionDetails.setValidationErrors(validationErrors);
        return validationExceptionDetails;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusinessExceptionDetails handleMethodArgumentTypeMismatchException(HttpServletRequest request) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail("Method argument type error");
        businessExceptionDetails.setStatus("400");
        businessExceptionDetails.setType(request.getRequestURI());
        return businessExceptionDetails;
    }


}
