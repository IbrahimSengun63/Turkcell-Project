package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "offers")
@Tag(name = "Offer Controller", description = "Manage offers in the system")
public class OfferController {
    private final OfferService offerService;

    @PostMapping("/add")
    @CacheEvict(value = "offer_list", allEntries = true)
    @Operation(summary = "Add Offer", description = "Adds a new offer to the database.")
    public ResponseEntity<ResponseAddOfferDTO> addOffer(@Valid @RequestBody RequestAddOfferDTO requestAddOfferDTO) {
        ResponseAddOfferDTO response = offerService.addOffer(requestAddOfferDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "offer_list", allEntries = true)
    @Operation(summary = "Update Offer", description = "Updates an existing offer based on the provided offer ID and update data.")
    public ResponseEntity<ResponseUpdateOfferDTO> updateOffer(@PathVariable @Valid @Min(value = 1) int id, @Valid @RequestBody RequestUpdateOfferDTO requestUpdateOfferDTO) {
        ResponseUpdateOfferDTO response = offerService.updateOffer(id, requestUpdateOfferDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/delete/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "offer_list", allEntries = true)
    @Operation(summary = "Delete Offer", description = "Deactivates the offer based on the provided offer ID.")
    public ResponseEntity<String> deleteOffer(@PathVariable @Valid @Min(value = 1) int id) {
        offerService.deleteOffer(id);
        return ResponseEntity.ok("Offer with ID " + id + " has been deleted successfully.");
    }
    @GetMapping
    @Cacheable(value = "offer_list")
    @Operation(summary = "Get All Offers", description = "Retrieves a list of all offers.")
    public ResponseEntity<List<GetAllResponseOfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    @Operation(summary = "Get Offer by ID", description = "Retrieves a specific offer based on the offer ID.")
    public ResponseEntity<GetResponseOfferDTO> getOffer(@PathVariable @Valid @Min(value = 1) int id) {
        return  ResponseEntity.ok(offerService.getOffer(id));
    }
}
