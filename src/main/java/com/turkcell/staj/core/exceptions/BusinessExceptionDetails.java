package com.turkcell.staj.core.exceptions;

public class BusinessExceptionDetails extends ExceptionDetails {
    public BusinessExceptionDetails() {
        setTitle("Business Rule Violation");
        setType("http://lms.com/exceptions/business");
        setStatus("400");
    }
}
