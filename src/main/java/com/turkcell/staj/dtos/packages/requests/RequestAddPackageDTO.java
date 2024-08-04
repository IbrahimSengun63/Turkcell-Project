package com.turkcell.staj.dtos.packages.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestAddPackageDTO {

    @NotBlank(message = "Package name can't be blank.")
    private String packageName;
    @NotBlank(message = "Description can't be blank.")
    private String description;
    @PositiveOrZero(message = "Price must be a positive value.")
    private double price;
    @NotNull(message = "Status can't be null.")
    private boolean status;
}
