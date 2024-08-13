package com.turkcell.staj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class OfferControllerTest {

    @InjectMocks
    private OfferController offerController;

    @Mock
    private OfferService offerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(offerController).build();
    }

    @Test
    public void shouldAddOffer() throws Exception {
        // Do
        RequestAddOfferDTO request = new  RequestAddOfferDTO("offername1", "desc1", 35.12, true);
        ResponseAddOfferDTO response = new ResponseAddOfferDTO(3, "offername1", "desc1", 35.12, true);
        // When
        when(offerService.addOffer(any(RequestAddOfferDTO.class))).thenReturn(response);
        // Act and Assert
        mockMvc.perform(post("/api/offers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerId").value(3))
                .andExpect(jsonPath("$.offerName").value("offername1"))
                .andExpect(jsonPath("$.description").value("desc1"))
                .andExpect(jsonPath("$.price").value(35.12))
                .andExpect(jsonPath("$.status").value(true))
                .andReturn();
        // Verify
        verify(offerService, times(1)).addOffer(any(RequestAddOfferDTO.class));
    }

    @Test
    public void shouldUpdateOffer() throws Exception {
        // Do
        int id = 3;
        RequestUpdateOfferDTO request = new  RequestUpdateOfferDTO("offername1", "desc1", 35.12, true);
        ResponseUpdateOfferDTO response = new ResponseUpdateOfferDTO(3, "offername1", "desc1", 35.12, true);

        // When
        when(offerService.updateOffer(eq(id), any(RequestUpdateOfferDTO.class))).thenReturn(response);

        // Act and Assert
        mockMvc.perform(put("/api/offers/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerId").value(3))
                .andExpect(jsonPath("$.offerName").value("offername1"))
                .andExpect(jsonPath("$.description").value("desc1"))
                .andExpect(jsonPath("$.price").value(35.12))
                .andExpect(jsonPath("$.status").value(true))
                .andReturn();
        // Verify
        verify(offerService, times(1)).updateOffer(eq(id), any(RequestUpdateOfferDTO.class));
    }

    @Test
    public void shouldDeleteOffer() throws Exception {
        // Do
        int id = 3;
        // Act and Assert
        mockMvc.perform(put("/api/offers/delete/" + id))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void shouldGetOffer() throws Exception {
        // Do
        int id = 3;
        GetResponseOfferDTO response = new GetResponseOfferDTO(id, "offername1", "desc1", 35.12, true);
        // When
        when(offerService.getOffer(id)).thenReturn(response);
        // Act and Assert
        mockMvc.perform(get("/api/offers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerId").value(3))
                .andExpect(jsonPath("$.offerName").value("offername1"))
                .andExpect(jsonPath("$.description").value("desc1"))
                .andExpect(jsonPath("$.price").value(35.12))
                .andExpect(jsonPath("$.status").value(true))
                .andReturn();
        // Verify
        verify(offerService, times(1)).getOffer(id);
    }

    @Test
    public void shouldGetAllOffers() throws Exception {
        // Do
        List<GetAllResponseOfferDTO> response = List.of(
                new GetAllResponseOfferDTO(1, "offername1", "desc1", 35.12, true),
                new GetAllResponseOfferDTO(2, "offername2", "desc2", 34.53, false)
        );
        // When
        when(offerService.getAllOffers()).thenReturn(response);
        // Act and Assert
        mockMvc.perform(get("/api/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].offerId").value(1))
                .andExpect(jsonPath("$[0].offerName").value("offername1"))
                .andExpect(jsonPath("$[0].description").value("desc1"))
                .andExpect(jsonPath("$[0].price").value(35.12))
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].offerId").value(2))
                .andExpect(jsonPath("$[1].offerName").value("offername2"))
                .andExpect(jsonPath("$[1].description").value("desc2"))
                .andExpect(jsonPath("$[1].price").value(34.53))
                .andExpect(jsonPath("$[1].status").value(false))
                .andReturn();
        // Verify
        verify(offerService, times(1)).getAllOffers();
    }

}
