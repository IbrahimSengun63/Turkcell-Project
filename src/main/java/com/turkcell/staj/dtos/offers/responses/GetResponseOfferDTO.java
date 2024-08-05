package com.turkcell.staj.dtos.offers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GetResponseOfferDTO {

    private int offerId;
    private String offerName;
    private String description;
    private double price;
    private boolean status;
}
