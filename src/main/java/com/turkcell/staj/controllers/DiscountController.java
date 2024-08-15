package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.DiscountService;
import com.turkcell.staj.dtos.discounts.requests.RequestAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.requests.RequestUpdateDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetOfferDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseUpdateDiscountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
@CacheConfig(cacheNames = "discounts")
@Tag(name = "Discount Controller", description = "Manage discounts in the system")
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping("/add")
    @Operation(summary = "Add Discount", description = "Adds a new discount to the database.")
    public ResponseEntity<ResponseAddDiscountDTO> addDiscount(@Valid @RequestBody RequestAddDiscountDTO request){
        ResponseAddDiscountDTO response = discountService.addDiscount(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    @Operation(summary = "Update Discount", description = "Updates an existing discount based on the provided discount ID and update data.")
    public ResponseEntity<ResponseUpdateDiscountDTO> updateDiscount(@PathVariable @Valid @Min(value = 1) int id, @Valid @RequestBody RequestUpdateDiscountDTO request){
        ResponseUpdateDiscountDTO response = discountService.updateDiscount(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    @Operation(summary = "Get Discount by ID", description = "Retrieves a specific discount based on the discount ID.")
    public ResponseEntity<ResponseGetDiscountDTO> getDiscount(@PathVariable @Valid @Min(value = 1) int id){
        ResponseGetDiscountDTO response = discountService.getDiscount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offer/{offerId}")
    @Cacheable(key = "#offerId")
    @Operation(summary = "Get Discount for Offer", description = "Retrieves the discount associated with a specific offer based on the offer ID.")
    public ResponseEntity<ResponseGetOfferDiscountDTO> getOfferDiscount(@PathVariable @Valid @Min(value = 1) int offerId){
        ResponseGetOfferDiscountDTO response = discountService.getOfferDiscount(offerId);
        return ResponseEntity.ok(response);
    }
}
