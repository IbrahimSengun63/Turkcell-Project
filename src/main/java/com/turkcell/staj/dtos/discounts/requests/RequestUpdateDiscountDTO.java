package com.turkcell.staj.dtos.discounts.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RequestUpdateDiscountDTO {
    @Min(value = 0, message = "Discount rate can't be negative.")
    @Max(value = 100, message = "Discount rate can't exceed 100.")
    private Integer discountRate;
    private Boolean status;
}
