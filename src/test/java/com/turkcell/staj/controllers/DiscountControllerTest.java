package com.turkcell.staj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.staj.business.abstracts.DiscountService;
import com.turkcell.staj.dtos.discounts.requests.RequestAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.requests.RequestUpdateDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.*;
import com.turkcell.staj.dtos.discounts.responses.ResponseAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseUpdateDiscountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DiscountControllerTest {

    @InjectMocks
    private DiscountController discountController;
    @Mock
    private DiscountService discountService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(discountController).build();
    }

    @Test
    public void shouldAddDiscount() throws Exception {
        // Do
        int discountId = 2;
        int discountRate = 50;
        int offerId = 3;
        boolean status = true;
        RequestAddDiscountDTO request = new  RequestAddDiscountDTO(discountRate, offerId, status);
        ResponseAddDiscountDTO response = new ResponseAddDiscountDTO(discountId, offerId, discountRate, status);
        // When
        when(discountService.addDiscount(any(RequestAddDiscountDTO.class))).thenReturn(response);
        // Act and Assert
        mockMvc.perform(post("/api/discounts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountId").value(discountId))
                .andExpect(jsonPath("$.discountRate").value(discountRate))
                .andExpect(jsonPath("$.offerId").value(offerId))
                .andExpect(jsonPath("$.status").value(status))
                .andReturn();
        // Verify
        verify(discountService, times(1)).addDiscount(any(RequestAddDiscountDTO.class));
    }

    @Test
    public void shouldUpdateDiscount() throws Exception {
        // Do
        int discountId = 2;
        int discountRate = 50;
        int offerId = 3;
        boolean status = true;
        RequestUpdateDiscountDTO request = new  RequestUpdateDiscountDTO(discountRate, status);
        ResponseUpdateDiscountDTO response = new ResponseUpdateDiscountDTO(discountId, offerId, discountRate, status);
        // When
        when(discountService.updateDiscount(eq(discountId), any(RequestUpdateDiscountDTO.class))).thenReturn(response);
        // Act and Assert
        mockMvc.perform(put("/api/discounts/update/" + discountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountId").value(discountId))
                .andExpect(jsonPath("$.offerId").value(offerId))
                .andExpect(jsonPath("$.discountRate").value(discountRate))
                .andExpect(jsonPath("$.status").value(status))
                .andReturn();
        // Verify
        verify(discountService, times(1)).updateDiscount(eq(discountId), any(RequestUpdateDiscountDTO.class));
    }

    @Test
    public void shouldGetDiscount() throws Exception {
        // Do
        int discountId = 2;
        int discountRate = 50;
        int offerId = 3;
        boolean status = true;
        ResponseGetDiscountDTO response = new ResponseGetDiscountDTO(discountId, offerId, discountRate, status);
        // When
        when(discountService.getDiscount(discountId)).thenReturn(response);
        // Act and Assert
        mockMvc.perform(get("/api/discounts/{id}", discountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountId").value(discountId))
                .andExpect(jsonPath("$.offerId").value(offerId))
                .andExpect(jsonPath("$.discountRate").value(discountRate))
                .andExpect(jsonPath("$.status").value(status))
                .andReturn();
        // Verify
        verify(discountService, times(1)).getDiscount(discountId);
    }

    @Test
    public void shouldGetOfferDiscount() throws Exception {
        // Do
        int discountId = 2;
        int discountRate = 50;
        int offerId = 3;
        boolean status = true;
        ResponseGetOfferDiscountDTO response = new ResponseGetOfferDiscountDTO(discountId, offerId, discountRate, status);
        // When
        when(discountService.getOfferDiscount(offerId)).thenReturn(response);
        // Act and Assert
        mockMvc.perform(get("/api/discounts/offer/{offerId}", offerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountId").value(discountId))
                .andExpect(jsonPath("$.offerId").value(offerId))
                .andExpect(jsonPath("$.discountRate").value(discountRate))
                .andExpect(jsonPath("$.status").value(status))
                .andReturn();
        // Verify
        verify(discountService, times(1)).getOfferDiscount(offerId);
    }
}