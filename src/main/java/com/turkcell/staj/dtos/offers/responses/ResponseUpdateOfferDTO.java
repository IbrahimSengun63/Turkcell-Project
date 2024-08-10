package com.turkcell.staj.dtos.offers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ResponseUpdateOfferDTO {
    private int offerId;
    private String offerName;
    private String description;
    private Double price;
    private Boolean status;
}
