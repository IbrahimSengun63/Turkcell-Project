package com.turkcell.staj.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusinessExceptionDetails handleBusinessException(BusinessException businessException) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail(businessException.getMessage());
        return businessExceptionDetails;
    }

    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessExceptionDetails handleNoSuchElementException(NoSuchElementException exception) {
        BusinessExceptionDetails businessExceptionDetails = new BusinessExceptionDetails();
        businessExceptionDetails.setDetail(exception.getMessage());
        businessExceptionDetails.setStatus("500");
        return businessExceptionDetails;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetails handleValidationException(MethodArgumentNotValidException ex) {
        ValidationExceptionDetails validationExceptionDetails = new ValidationExceptionDetails();
        validationExceptionDetails.setDetail("VALIDATION.EXCEPTION");
        validationExceptionDetails.setValidationErrors(
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(Collectors.toMap(error -> Objects.requireNonNull(error.getCodes())[error.getCodes().length - 1],
                                error -> error.getField() + " field " + error.getDefaultMessage()))
        );


        return validationExceptionDetails;
    }
}
