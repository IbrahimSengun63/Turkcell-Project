package com.turkcell.staj.dtos.discounts.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUpdateDiscountDTO {
    private Integer discountId;
    private Integer offerId;
    private Integer discountRate;
    private Boolean status;
}
