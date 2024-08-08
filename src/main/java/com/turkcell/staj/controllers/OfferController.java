package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping("/add")
    public ResponseEntity<ResponseAddOfferDTO> addOffer(@Valid @RequestBody RequestAddOfferDTO requestAddOfferDTO) {
        ResponseAddOfferDTO response = this.offerService.addOffer(requestAddOfferDTO);
        return ResponseEntity.ok(response);
    }
}
