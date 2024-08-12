package com.turkcell.staj.dtos.discounts.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetDiscountDTO {
    private Integer discountId;
    private Integer offerId;
    private Integer discountRate;
    private Boolean status;

}
