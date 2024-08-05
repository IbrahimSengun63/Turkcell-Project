package com.turkcell.staj.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter

@AllArgsConstructor
public class ValidationExceptionDetails extends ExceptionDetails {
    public ValidationExceptionDetails(){
        setTitle("Validation Rule Violation");
        setDetail("Validation Problem");
        setType("http://lms.com/exceptions/validation");
        setStatus("400");
    }

    private Map<String, String> validationErrors;
}

