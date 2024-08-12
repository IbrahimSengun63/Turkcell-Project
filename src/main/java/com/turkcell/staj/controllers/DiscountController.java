package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.DiscountService;
import com.turkcell.staj.dtos.discounts.requests.RequestAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.requests.RequestUpdateDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetOfferDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseUpdateDiscountDTO;
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
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping("/add")
    public ResponseEntity<ResponseAddDiscountDTO> addDiscount(@Valid @RequestBody RequestAddDiscountDTO request){
        ResponseAddDiscountDTO response = discountService.addDiscount(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    public ResponseEntity<ResponseUpdateDiscountDTO> updateDiscount(@PathVariable @Valid @Min(value = 1) int id, @Valid @RequestBody RequestUpdateDiscountDTO request){
        ResponseUpdateDiscountDTO response = discountService.updateDiscount(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public ResponseEntity<ResponseGetDiscountDTO> getDiscount(@PathVariable @Valid @Min(value = 1) int id){
        ResponseGetDiscountDTO response = discountService.getDiscount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offer/{offerId}")
    @Cacheable(key = "#offerId")
    public ResponseEntity<ResponseGetOfferDiscountDTO> getOfferDiscount(@PathVariable @Valid @Min(value = 1) int offerId){
        ResponseGetOfferDiscountDTO response = discountService.getOfferDiscount(offerId);
        return ResponseEntity.ok(response);
    }
}
