package com.turkcell.staj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldAddTransaction() throws Exception {
        // Arrange
        RequestAddTransactionDTO request = new RequestAddTransactionDTO(1, 1, Status.COMPLETED, null);
        ResponseAddTransactionDTO response = new ResponseAddTransactionDTO(1, 1, 1, 22.0, 22.0, Status.COMPLETED, null, 22.0);


        // Use ArgumentMatchers.any()
        when(transactionService.addTransaction(any(RequestAddTransactionDTO.class))).thenReturn(response);

        // Act
        mockMvc.perform(post("/api/transactions/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.offerId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.offerPrice").value(22.0))
                .andExpect(jsonPath("$.price").value(22.0))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.createdDate").doesNotExist())
                .andExpect(jsonPath("$.userBalanceAfterTransaction").value(22.0))
                .andReturn();


        // Verify that the service method was called once with any RequestAddReviewDTO object
        verify(transactionService, times(1)).addTransaction(any(RequestAddTransactionDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenAddTransactionValidationsFailed() throws Exception {
        // Arrange
        RequestAddTransactionDTO request = new RequestAddTransactionDTO(-1, -1, Status.COMPLETED, null);

        // Act
        mockMvc.perform(post("/api/transactions/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        // Verify that the service method was never called due to validation failure
        verify(transactionService, never()).addTransaction(any(RequestAddTransactionDTO.class));
    }

}