package com.turkcell.staj.dtos.offers.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

public class RequestAddOfferDTO {

    @NotBlank(message = "Offer name can't be blank.")
    @Size(max = 255, message = "length must be in the range {1,255}")
    private String offerName;
    @NotBlank(message = "Description can't be blank.")
    private String description;
    @PositiveOrZero(message = "Price must be a positive value.")
    private double price;
    @NotNull(message = "Status can't be null.")
    private boolean status;
}
