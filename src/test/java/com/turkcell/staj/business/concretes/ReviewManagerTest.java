package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.business.rules.ReviewBusinessRules;
import com.turkcell.staj.business.rules.TransactionBusinessRules;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.*;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.entities.Review;
import com.turkcell.staj.entities.Transaction;
import com.turkcell.staj.entities.User;
import com.turkcell.staj.mappers.ReviewMapper;
import com.turkcell.staj.repositories.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewManagerTest {

    @InjectMocks
    private ReviewManager reviewManager;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewMapper reviewMapper;
    @Mock
    private TransactionService transactionService;
    @Mock
    private OfferService offerService;
    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void shouldGetAllOfferReviews() {
        // Arrange
        int offerId = 1;
        Offer offer = new Offer();
        offer.setId(offerId);
        User user = new User();
        user.setId(1);

        List<Review> reviews = List.of(
                new Review(1, offer, user, 5, "c", LocalDate.now()),
                new Review(2, offer, user, 4, "s", LocalDate.now())
        );

        ResponseGetAllOfferReviewDTO response1 = new ResponseGetAllOfferReviewDTO(
                1, offerId, user.getId(), 5, "c", LocalDate.now()
        );
        ResponseGetAllOfferReviewDTO response2 = new ResponseGetAllOfferReviewDTO(
                2, offerId, user.getId(), 4, "s", LocalDate.now()
        );
        List<ResponseGetAllOfferReviewDTO> responseList = List.of(response1, response2);

        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(reviewRepository.findByOfferId(offerId)).thenReturn(reviews);
        when(reviewMapper.reviewsToResponseGetAllOfferReviewsDto(reviews)).thenReturn(responseList);

        // Act
        GetOfferReviewsWrapper result = reviewManager.getAllOfferReviews(offerId);

        // Assert
        assertNotNull(result);
        assertEquals(responseList, result.getOfferReviews());
        assertEquals(4.5, result.getOfferAvgRating(), 0.01);

        // Verify interactions
        verify(offerService, times(1)).getOfferById(offerId);
        verify(reviewRepository, times(1)).findByOfferId(offerId);
        verify(reviewMapper, times(1)).reviewsToResponseGetAllOfferReviewsDto(reviews);
    }

    @Test
    void shouldThrowExceptionWhenGettingOfferReviewsWhenOfferIsNull() {
        int offerId = 1;
        when(offerService.getOfferById(offerId)).thenThrow(new BusinessException("Offer can't be null"));

        assertThrows(BusinessException.class, () -> reviewManager.getAllOfferReviews(offerId));
        // Verify interactions and call counts
        verify(offerService, times(1)).getOfferById(offerId);
        verify(reviewRepository, never()).findByOfferId(anyInt());
        verify(reviewMapper, never()).reviewsToResponseGetAllOfferReviewsDto(anyList());

    }

    @Test
    void shouldReturnEmptyReviewsWhenNoOfferReviewsFound() {
        int offerId = 1;
        Offer offer = new Offer();
        offer.setId(offerId);

        List<Review> reviews = Collections.emptyList();

        List<ResponseGetAllOfferReviewDTO> responseList = Collections.emptyList();
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(reviewRepository.findByOfferId(offerId)).thenReturn(reviews);
        when(reviewMapper.reviewsToResponseGetAllOfferReviewsDto(reviews)).thenReturn(responseList);

        GetOfferReviewsWrapper wrapper = reviewManager.getAllOfferReviews(offerId);
        assertNotNull(wrapper);
        assertEquals(responseList, wrapper.getOfferReviews());
        assertEquals(0.0, wrapper.getOfferAvgRating());

        verify(offerService, times(1)).getOfferById(offerId);
        verify(reviewRepository, times(1)).findByOfferId(offerId);
        verify(reviewMapper, times(1)).reviewsToResponseGetAllOfferReviewsDto(reviews);
    }

    @Test
    void shouldGetAllUserReviews() {
        // arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);
        Offer offer = new Offer();

        List<Review> reviews = List.of(
                new Review(1, offer, user, 5, "c", LocalDate.now()),
                new Review(2, offer, user, 4, "s", LocalDate.now())
        );

        List<ResponseGetAllUserReviewDTO> responseList = List.of(
                new ResponseGetAllUserReviewDTO(1, 1, 1, 5, "c", LocalDate.now()),
                new ResponseGetAllUserReviewDTO(2, 1, 1, 5, "c", LocalDate.now())
        );

        // when
        when(userService.getUserById(userId)).thenReturn(user);
        when(reviewRepository.findByUserId(userId)).thenReturn(reviews);
        when(reviewMapper.reviewsToResponseGetAllUserReviewsDto(reviews)).thenReturn(responseList);

        //act
        List<ResponseGetAllUserReviewDTO> result = reviewManager.getAllUserReviews(userId);

        //assert
        assertNotNull(result);
        assertEquals(responseList, result);

        //Verify
        verify(userService, times(1)).getUserById(userId);
        verify(reviewRepository, times(1)).findByUserId(userId);
        verify(reviewMapper, times(1)).reviewsToResponseGetAllUserReviewsDto(reviews);

    }

    @Test
    void shouldThrowExceptionWhenGettingUserReviewsWhenUserIsNull() {
        // arrange
        int userId = 1;

        // when
        when(userService.getUserById(userId)).thenThrow(new BusinessException("User can't be null"));

        // act & assert
        assertThrows(BusinessException.class, () -> reviewManager.getAllUserReviews(userId));

        //verify
        verify(userService, times(1)).getUserById(userId);
        verify(reviewRepository, never()).findByUserId(userId);
        verify(reviewMapper, never()).reviewsToResponseGetAllUserReviewsDto(anyList());

    }

    @Test
    void shouldReturnEmptyReviewsWhenNoUserReviewsFound() {
        // arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);

        List<Review> reviews = Collections.emptyList();
        List<ResponseGetAllUserReviewDTO> responseList = Collections.emptyList();

        // when
        when(userService.getUserById(userId)).thenReturn(user);
        when(reviewRepository.findByUserId(userId)).thenReturn(reviews);
        when(reviewMapper.reviewsToResponseGetAllUserReviewsDto(reviews)).thenReturn(responseList);

        //act
        List<ResponseGetAllUserReviewDTO> result = reviewManager.getAllUserReviews(userId);

        //assert
        assertNotNull(result);
        assertEquals(responseList, result);
        assertEquals(0, result.size());

        //verify
        verify(userService, times(1)).getUserById(userId);
        verify(reviewRepository, times(1)).findByUserId(userId);
        verify(reviewMapper, times(1)).reviewsToResponseGetAllUserReviewsDto(reviews);

    }

    @Test
    void shouldAddReview() {
        // arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);

        int offerId = 1;
        Offer offer = new Offer();
        offer.setId(offerId);

        Review review = new Review(1, offer, user, 1, "s", LocalDate.now());
        Review savedReview = new Review(1, offer, user, 1, "s", LocalDate.now());
        RequestAddReviewDTO request = new RequestAddReviewDTO(1, 1, 1, "s", LocalDate.now());
        ResponseAddReviewDTO response = new ResponseAddReviewDTO(1, 1, 1, 1, "s", LocalDate.now());

        // when
        when(userService.getUserById(userId)).thenReturn(user);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(transactionService.checkIfUserPurchasedOffer(userId, offerId)).thenReturn(true);
        when(reviewMapper.requestAddReviewDtoToReview(request)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(savedReview);
        when(reviewMapper.reviewToResponseAddReviewDTO(savedReview)).thenReturn(response);

        // act
        ResponseAddReviewDTO result = reviewManager.addReview(request);

        // assert
        assertNotNull(result);
        assertEquals(response, result);

        //verify
        verify(userService, times(1)).getUserById(userId);
        verify(offerService, times(1)).getOfferById(offerId);
        verify(transactionService, times(1)).checkIfUserPurchasedOffer(userId, offerId);
        verify(reviewMapper, times(1)).requestAddReviewDtoToReview(request);
        verify(reviewMapper, times(1)).reviewToResponseAddReviewDTO(savedReview);
    }

    @Test
    void shouldThrowExceptionWhenAddReviewAndNoUserFound() {
        // arrange
        int userId = 1;
        RequestAddReviewDTO request = new RequestAddReviewDTO();
        request.setUserId(userId);
        // when
        when(userService.getUserById(userId)).thenThrow(new BusinessException("User can't be null"));

        // act & assert
        assertThrows(BusinessException.class, () -> reviewManager.addReview(request));

        //verify
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void shouldThrowExceptionWhenAddReviewAndNoOfferFound() {
        // arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);

        int offerId = 1;

        RequestAddReviewDTO request = new RequestAddReviewDTO();
        request.setOfferId(offerId);
        request.setUserId(userId);

        // when
        when(userService.getUserById(userId)).thenReturn(user);
        when(offerService.getOfferById(offerId)).thenThrow(new BusinessException("Offer can't be null"));

        // act & assert
        assertThrows(BusinessException.class, () -> reviewManager.addReview(request));

        // verify
        verify(userService,times(1)).getUserById(userId);
        verify(offerService,times(1)).getOfferById(offerId);
    }

    @Test
    void shouldThrowExceptionWhenAddReviewUserNotPurchasedOffer() {
        // arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);

        int offerId = 1;
        Offer offer = new Offer();
        offer.setId(offerId);

        RequestAddReviewDTO request = new RequestAddReviewDTO();
        request.setUserId(userId);
        request.setOfferId(offerId);

        // when
        when(userService.getUserById(userId)).thenReturn(user);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(transactionService.checkIfUserPurchasedOffer(userId,offerId)).thenReturn(false);

        // act & assert
        assertThrows(BusinessException.class,()-> reviewManager.addReview(request));

        //verify
        verify(userService,times(1)).getUserById(userId);
        verify(offerService,times(1)).getOfferById(offerId);
        verify(transactionService,times(1)).checkIfUserPurchasedOffer(userId,offerId);
    }

}