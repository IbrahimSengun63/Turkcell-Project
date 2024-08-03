package com.turkcell.staj.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExceptionDetails {

    private String title;
    private String detail;
    private String status;
    private String type;
}
