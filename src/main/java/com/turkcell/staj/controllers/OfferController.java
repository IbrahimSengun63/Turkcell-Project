package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.entities.Offer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<List<GetAllResponseOfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetResponseOfferDTO> getOffer(@PathVariable @Valid @Min(value = 1) int id) {
        return  ResponseEntity.ok(offerService.getOffer(id));
    }
}
