package com.turkcell.staj.dtos.user.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ResponseAddUserDTO {

    private int userId;
    private String name;
    private String surname;
    private double balance;
}