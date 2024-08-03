package com.turkcell.staj.dtos.packages.requests;

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

public class RequestUpdatePackageDTO {

    private int packageId;
    @NotBlank
    private String packageName;
    @NotBlank
    private String description;
    @Positive
    private double price;
    private boolean status;
}
