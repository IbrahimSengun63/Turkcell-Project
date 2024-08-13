package com.turkcell.staj.dtos.user.requests;

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

    @Size(min = 3, max = 255, message = "Length must be in range {3,255} ")
    private String name;
    @Size(min = 3, max = 255, message = "Length must be in range {3,255} ")
    private String surname;
    @PositiveOrZero(message = "Balance must be zero or positive")
    private Double balance;

}