package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseUpdateOfferDTO> updateOffer(@PathVariable @Valid @Min(value = 1) int id, @Valid @RequestBody RequestUpdateOfferDTO requestUpdateOfferDTO) {
        ResponseUpdateOfferDTO response = this.offerService.updateOffer(id, requestUpdateOfferDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable @Valid @Min(value = 1) int id) {
        this.offerService.deleteOffer(id);
        return ResponseEntity.ok("Offer with ID " + id + " has been deleted successfully.");
    }
    @GetMapping
    public ResponseEntity<List<GetAllResponseOfferDTO>> getAllOffers() {
        return ResponseEntity.ok(this.offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetResponseOfferDTO> getOffer(@PathVariable @Valid @Min(value = 1) int id) {
        return  ResponseEntity.ok(this.offerService.getOffer(id));
    }
}
