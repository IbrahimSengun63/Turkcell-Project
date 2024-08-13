package com.turkcell.staj.core.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
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
        businessExceptionDetails.setPath(request.getRequestURI());
        return businessExceptionDetails;
    }

    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessExceptionDetails handleNoSuchElementException(NoSuchElementException exception, HttpServletRequest request) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail(exception.getMessage());
        businessExceptionDetails.setStatus("500");
        businessExceptionDetails.setPath(request.getRequestURI());
        return businessExceptionDetails;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDetails handleParseErrors(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ValidationExceptionDetails validationExceptionDetails = new ValidationExceptionDetails();
        validationExceptionDetails.setDetail("Parse Error");
        validationExceptionDetails.setStatus("400");
        validationExceptionDetails.setPath(request.getRequestURI());

        Map<String, String> validationErrors = new HashMap<>();
        String errorMessage = ex.getMessage();

        // Add the error message to the validation errors map
        validationErrors.put("message", errorMessage);

        validationExceptionDetails.setValidationErrors(validationErrors);
        return validationExceptionDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDetails handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Create a new instance of ValidationExceptionDetails to encapsulate validation errors
        ValidationExceptionDetails validationExceptionDetails = new ValidationExceptionDetails();
        // Set the message for the validation exception
        validationExceptionDetails.setDetail("VALIDATION.EXCEPTION");
        validationExceptionDetails.setPath(request.getRequestURI());
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
        validationExceptionDetails.setPath(request.getRequestURI());

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
    public ValidationExceptionDetails handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ValidationExceptionDetails validationExceptionDetails = new ValidationExceptionDetails();
        validationExceptionDetails.setDetail("Method argument type error");
        validationExceptionDetails.setStatus("400");
        validationExceptionDetails.setPath(request.getRequestURI());

        Map<String, String> validationErrors = new HashMap<>();
        // Extracting parameter name and error message
        String parameterName = ex.getName();
        String message = "Invalid value for parameter: " + parameterName;

        // Add to validation errors map
        validationErrors.put("parameter", message);

        // You might want to extract path variable errors similarly
        // For path variables, you need additional context from the request or exception

        validationExceptionDetails.setValidationErrors(validationErrors);
        return validationExceptionDetails;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessExceptionDetails handleDataIntegrityViolationException(HttpServletRequest request) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail("An internal error occurred. Please try again later.");
        businessExceptionDetails.setStatus("500");
        businessExceptionDetails.setPath(request.getRequestURI());
        return businessExceptionDetails;
    }


}
