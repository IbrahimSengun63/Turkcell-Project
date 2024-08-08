package com.turkcell.staj.dtos.user.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestUpdateUserDTO {

    @Min(value = 1, message = "UserId must be a positive integer greater than or equal to 1.")
    private int userId;
    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;
    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String surname;
    @PositiveOrZero(message = "Balance must be zero or positive")
    private double balance;
}