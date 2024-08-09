package com.turkcell.staj.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter

@AllArgsConstructor
public class ValidationExceptionDetails extends ExceptionDetails {
    public ValidationExceptionDetails(){
        setTitle("Validation Rule Violation");
        setDetail("Validation Problem");
        setPath("http://turkcell-staj.com/exceptions/validation");
        setStatus("400");
    }
    private Map<String, String> validationErrors;
}

