package com.turkcell.staj.dtos.offers.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestUpdateOfferDTO {
    @Size(min = 3, max = 255, message = "length must be in the range {3,255}")
    private String offerName;
    @Size(min = 3, max = 255, message = "length must be in the range {3,255}")
    private String description;
    @PositiveOrZero(message = "Price must be a positive value.")
    private double price;
    @NotNull(message = "Status can't be null.")
    private boolean status;
}
