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

    @NotNull(message = "Offer name can't be null")
    @Size(min = 3, max = 255, message = "length must be in the range {3,255}")
    private String offerName;
    @NotNull(message = "Offer description can't be null")
    @Size(min = 3, max = 255, message = "length must be in the range {3,255}")
    private String description;
    @NotNull(message = "Offer price can't be null")
    @PositiveOrZero(message = "Price must be a positive value.")
    private Double price;
    @NotNull(message = "Status can't be null.")
    private Boolean status;
}
