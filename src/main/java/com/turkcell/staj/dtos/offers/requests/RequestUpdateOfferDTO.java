package com.turkcell.staj.dtos.offers.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestUpdateOfferDTO {

    @Min(value = 1, message = "OfferId must be a positive integer greater than or equal to 1.")
    private int offerId;
    @NotBlank(message = "Offer name can't be blank.")
    private String offerName;
    @NotBlank(message = "Description can't be blank.")
    private String description;
    @Positive(message = "Price must be a positive value.")
    private double price;
    @NotNull(message = "Status can't be null.")
    private boolean status;
}
