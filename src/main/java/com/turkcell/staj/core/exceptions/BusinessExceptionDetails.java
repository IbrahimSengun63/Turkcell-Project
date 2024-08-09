package com.turkcell.staj.core.exceptions;

public class BusinessExceptionDetails extends ExceptionDetails {
    public BusinessExceptionDetails() {
        setTitle("Business Rule Violation");
        setPath("http://turkcell-staj.com/exceptions/business");
        setStatus("400");
    }
}
