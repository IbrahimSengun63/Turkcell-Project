package com.turkcell.staj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllOfferReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private HazelcastInstance hazelcastInstance;


    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldGetAllOfferReviews() throws Exception {
        // Arrange
        int offerId = 1;
        GetOfferReviewsWrapper response = new GetOfferReviewsWrapper(
                List.of(
                        new ResponseGetAllOfferReviewDTO(1, offerId, 1, 2, "comment", null)
                ),
                2
        );

        when(reviewService.getAllOfferReviews(offerId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/offer/{offerId}", offerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerReviews[0].reviewId").value(1))
                .andExpect(jsonPath("$.offerReviews[0].offerId").value(offerId))
                .andExpect(jsonPath("$.offerReviews[0].userId").value(1))
                .andExpect(jsonPath("$.offerReviews[0].rating").value(2))
                .andExpect(jsonPath("$.offerReviews[0].comment").value("comment"))
                .andExpect(jsonPath("$.offerAvgRating").value(2));

        verify(reviewService, times(1)).getAllOfferReviews(offerId);
    }

    @Test
    void shouldGetAllOfferReviewsWhenFailToFindOfferReviews() throws Exception {
        // Arrange
        int offerId = 1;
        GetOfferReviewsWrapper response = new GetOfferReviewsWrapper(List.of(),2);

        // when
        when(reviewService.getAllOfferReviews(offerId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/offer/{offerId}", offerId))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));


        verify(reviewService, times(1)).getAllOfferReviews(offerId);
    }

    @Test
    void shouldReturnBadRequestForInvalidUserIdWhenGettingOfferReviews() throws Exception {
        // Arrange
        int invalidOfferId = -1;

        // Act & Assert
        mockMvc.perform(get("/api/reviews/offer/{offerId}", invalidOfferId))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).getAllUserReviews(invalidOfferId);
    }

    @Test
    void shouldGetAllUserReviews() throws Exception {
        // Arrange
        int userId = 1;
        List<ResponseGetAllUserReviewDTO> response = List.of(
                new ResponseGetAllUserReviewDTO(1, 1,userId, 5, "c",null),
                new ResponseGetAllUserReviewDTO(2, 1,userId, 5, "c",null)
        );

        // when
        when(reviewService.getAllUserReviews(userId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId").value(1))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].comment").value("c"))
                .andExpect(jsonPath("$[1].reviewId").value(2))
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].rating").value(5))
                .andExpect(jsonPath("$[1].comment").value("c"));


        verify(reviewService, times(1)).getAllUserReviews(userId);
    }

    @Test
    void shouldReturnEmptyListWhenNoUserReviewsFound() throws Exception {
        // Arrange
        int userId = 1;
        List<ResponseGetAllUserReviewDTO> response = List.of(); // Empty list

        when(reviewService.getAllUserReviews(userId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(reviewService, times(1)).getAllUserReviews(userId);
    }

    @Test
    void shouldReturnBadRequestForInvalidUserIdWhenGettingUserReviews() throws Exception {
        // Arrange
        int invalidUserId = -1; // Assuming -1 is an invalid user ID

        // Act & Assert
        mockMvc.perform(get("/api/reviews/user/{userId}", invalidUserId))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).getAllUserReviews(invalidUserId);
    }

    @Test
    void shouldAddReviewSuccessfully() throws Exception {
        // Arrange
        RequestAddReviewDTO request = new RequestAddReviewDTO(1, 1, 5, "Valid comment", null);
        ResponseAddReviewDTO response = new ResponseAddReviewDTO(1, 1, 1, 5, "Valid comment", null);

        // Use ArgumentMatchers.any() to match any RequestAddReviewDTO object
        when(reviewService.addReview(any(RequestAddReviewDTO.class))).thenReturn(response);

        // Act
        MvcResult result = mockMvc.perform(post("/api/reviews/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1))
                .andExpect(jsonPath("$.offerId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Valid comment"))
                .andExpect(jsonPath("$.createdDate").doesNotExist())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        // Verify that the service method was called once with any RequestAddReviewDTO object
        verify(reviewService, times(1)).addReview(any(RequestAddReviewDTO.class));
    }



















    @Test
    void getAllUserReviews() {
    }

    @Test
    void addReview() {
    }

    @Test
    void updateReview() {
    }

    @Test
    void getReview() {
    }
}