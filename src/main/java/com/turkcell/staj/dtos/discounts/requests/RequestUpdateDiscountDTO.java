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
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1.")
    private Integer offerId;
    private Boolean status;
}
