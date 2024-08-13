package com.turkcell.staj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.*;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

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
    void shouldReturnEmptyListWhenFailToFindOfferReviews() throws Exception {
        // Arrange
        int offerId = 1;
        GetOfferReviewsWrapper response = new GetOfferReviewsWrapper(List.of(),0);

        // when
        when(reviewService.getAllOfferReviews(offerId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/offer/{offerId}", offerId))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));


        verify(reviewService, times(1)).getAllOfferReviews(offerId);
    }

    @Test
    void shouldReturnBadRequestForInvalidOfferIdWhenGettingOfferReviews() throws Exception {
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
    void shouldAddReview() throws Exception {
        // Arrange
        RequestAddReviewDTO request = new RequestAddReviewDTO(1, 1, 5, "Valid comment", null);
        ResponseAddReviewDTO response = new ResponseAddReviewDTO(1, 1, 1, 5, "Valid comment", null);

        // Use ArgumentMatchers.any() to match any RequestAddReviewDTO object
        when(reviewService.addReview(any(RequestAddReviewDTO.class))).thenReturn(response);

        // Act
        mockMvc.perform(post("/api/reviews/add")
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


        // Verify that the service method was called once with any RequestAddReviewDTO object
        verify(reviewService, times(1)).addReview(any(RequestAddReviewDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenAddReviewValidationsFailed() throws Exception {
        // Arrange
        RequestAddReviewDTO request = new RequestAddReviewDTO(1, 1, -5, "c", null);
        ResponseAddReviewDTO response = new ResponseAddReviewDTO(1, 1, 1, -5, "c", null);

        // Use ArgumentMatchers.any() to match any RequestAddReviewDTO object
        when(reviewService.addReview(any(RequestAddReviewDTO.class))).thenReturn(response);

        // Act
        mockMvc.perform(post("/api/reviews/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();


        // Verify that the service method was called once with any RequestAddReviewDTO object
        verify(reviewService, never()).addReview(any(RequestAddReviewDTO.class));
    }

    @Test
    void shouldUpdateReview() throws Exception {
        // Arrange
        int id = 1;
        RequestUpdateReviewDTO request = new RequestUpdateReviewDTO(4, "ccc");
        ResponseUpdateReviewDTO response = new ResponseUpdateReviewDTO(id, 1, 1, 4, "ccc", null);

        // Use ArgumentMatchers.any() to match any RequestUpdateReviewDTO object
        when(reviewService.updateReview(eq(id),any(RequestUpdateReviewDTO.class))).thenReturn(response);

        // Act
        mockMvc.perform(put("/api/reviews/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(id))
                .andExpect(jsonPath("$.offerId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.comment").value("ccc"))
                .andExpect(jsonPath("$.createdDate").doesNotExist())
                .andReturn();

        // Verify that the service method was called once with any RequestUpdateReviewDTO object and the specific reviewId
        verify(reviewService, times(1)).updateReview(eq(id),any(RequestUpdateReviewDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateReviewValidationsFailed() throws Exception {
        // Arrange
        int id = 1;
        RequestUpdateReviewDTO request = new RequestUpdateReviewDTO(4, "c");
        ResponseUpdateReviewDTO response = new ResponseUpdateReviewDTO(id, 1, 1, 4, "c", null);

        // Use ArgumentMatchers.any() to match any RequestUpdateReviewDTO object
        when(reviewService.updateReview(eq(id),any(RequestUpdateReviewDTO.class))).thenReturn(response);

        // Act
        mockMvc.perform(put("/api/reviews/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Verify that the service method was called once with any RequestUpdateReviewDTO object and the specific reviewId
        verify(reviewService, never()).updateReview(eq(id),any(RequestUpdateReviewDTO.class));
    }

    @Test
    void shouldReturnBadRequestForInvalidIdWhenUpdatingReviews() throws Exception {
        // Arrange
        int id = -1;
        RequestUpdateReviewDTO request = new RequestUpdateReviewDTO(4, "ccc");
        ResponseUpdateReviewDTO response = new ResponseUpdateReviewDTO(id, 1, 1, 4, "ccc", null);

        // Use ArgumentMatchers.any() to match any RequestUpdateReviewDTO object
        when(reviewService.updateReview(eq(id),any(RequestUpdateReviewDTO.class))).thenReturn(response);
        // Act & Assert
        mockMvc.perform(put("/api/reviews/update/{id}", id))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).updateReview(id,request);
    }

    @Test
    void shouldGetReview() throws Exception {
        // Arrange
        int id = 1;
        ResponseGetReviewDTO response = new ResponseGetReviewDTO(id,1,1,5,"ccc",null);

        when(reviewService.getReview(id)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(id))
                .andExpect(jsonPath("$.offerId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("ccc"))
                .andExpect(jsonPath("$.createdDate").doesNotExist())
                .andReturn();

        verify(reviewService, times(1)).getReview(id);
    }

    @Test
    void shouldReturnBadRequestForInvalidIdWhenGettingReviews() throws Exception {
        // Arrange
        int id = -1;

        // Act & Assert
        mockMvc.perform(get("/api/reviews/{id}", id))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).getReview(id);
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