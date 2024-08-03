package com.turkcell.staj.dtos.user.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestUpdateUserDTO {

    private int userId;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @Positive
    private double balance;
}