package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.*;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.entities.Review;
import com.turkcell.staj.entities.User;
import com.turkcell.staj.mappers.ReviewMapper;
import com.turkcell.staj.repositories.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    void shouldFailToGetOfferReviewsWhenOfferIsNullAndThrowException() {
        int offerId = 1;
        when(offerService.getOfferById(offerId)).thenThrow(new BusinessException("Offer can't be null"));

        assertThrows(BusinessException.class, () -> reviewManager.getAllOfferReviews(offerId));
        // Verify interactions and call counts
        verify(offerService, times(1)).getOfferById(offerId);
        verify(reviewRepository, never()).findByOfferId(anyInt());
        verify(reviewMapper, never()).reviewsToResponseGetAllOfferReviewsDto(anyList());

    }

    @Test
    void shouldReturnEmptyReviewsListWhenOfferHasNoReviews() {
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
    void shouldFailToGetUserReviewsWhenUserIsNullAndThrowException() {
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
    void shouldReturnEmptyReviewsListWhenUserHasNoReviews() {
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
    void shouldFailToAddReviewWithoutUserAndThrowException() {
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
    void shouldFailToAddReviewWithoutOfferAndThrowException() {
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
        verify(userService, times(1)).getUserById(userId);
        verify(offerService, times(1)).getOfferById(offerId);
    }

    @Test
    void shouldFailToAddReviewWhenUserDidNotPurchaseOfferAndThrowException() {
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
        when(transactionService.checkIfUserPurchasedOffer(userId, offerId)).thenReturn(false);

        // act & assert
        assertThrows(BusinessException.class, () -> reviewManager.addReview(request));

        //verify
        verify(userService, times(1)).getUserById(userId);
        verify(offerService, times(1)).getOfferById(offerId);
        verify(transactionService, times(1)).checkIfUserPurchasedOffer(userId, offerId);
    }

    @Test
    void shouldUpdateReview() {
        // arrange
        int reviewId = 1;
        Review review = new Review();
        review.setId(reviewId);

        int userId = 1;
        User user = new User();
        user.setId(userId);

        int offerId = 1;
        Offer offer = new Offer();
        offer.setId(offerId);

        RequestUpdateReviewDTO request = new RequestUpdateReviewDTO();

        Review savedReview = new Review(1, offer, user, 1, "g", LocalDate.now());
        ResponseUpdateReviewDTO response = new ResponseUpdateReviewDTO(1, 1, 1, 1, "g", LocalDate.now());

        // when
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        doNothing().when(reviewMapper).updateReviewFromRequestUpdateReviewDto(request, review);
        when(reviewRepository.save(review)).thenReturn(savedReview);
        when(reviewMapper.reviewToResponseUpdateReviewDTO(savedReview)).thenReturn(response);

        //assert
        ResponseUpdateReviewDTO result = reviewManager.updateReview(reviewId, request);
        assertNotNull(result);
        assertEquals(response, result);

        //verify
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewMapper, times(1)).updateReviewFromRequestUpdateReviewDto(request, review);
        verify(reviewRepository, times(1)).save(review);
        verify(reviewMapper, times(1)).reviewToResponseUpdateReviewDTO(savedReview);

    }

    @Test
    void shouldFailToUpdateReviewWithoutReviewAndThrowException() {
        // arrange
        int reviewId = 1;
        Review review = new Review();
        review.setId(reviewId);

        RequestUpdateReviewDTO request = new RequestUpdateReviewDTO();


        // when
        when(reviewRepository.findById(reviewId)).thenThrow(new BusinessException("Review can't be null"));

        // act & assert
        assertThrows(BusinessException.class, () -> reviewManager.updateReview(reviewId, request));

        // verify
        verify(reviewRepository, times(1)).findById(reviewId);

    }

    @Test
    void shouldGetReview() {
        // arrange
        int reviewId = 1;
        Review review = new Review();
        review.setId(reviewId);

        ResponseGetReviewDTO response = new ResponseGetReviewDTO();
        response.setReviewId(reviewId);

        // when
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewMapper.reviewToResponseGetReviewDto(review)).thenReturn(response);

        // act
        ResponseGetReviewDTO result = reviewManager.getReview(reviewId);

        // assert
        assertNotNull(result);
        assertEquals(response, result);

        //verify
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewMapper, times(1)).reviewToResponseGetReviewDto(review);

    }

    @Test
    void shouldFailToRetrieveReviewAndThrowExceptionWhenReviewDoesNotExist() {
        // arrange
        int reviewId = 1;
        Review review = new Review();
        review.setId(reviewId);

        // when
        when(reviewRepository.findById(reviewId)).thenThrow(new BusinessException("Review can't be null"));

        // act & assert
        assertThrows(BusinessException.class, () -> reviewManager.getReview(reviewId));

        //verify
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void shouldFailToGetReviewByIdWhenReviewDoesNotExist() {
        // arrange
        int reviewId = 1;

        // when
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(BusinessException.class, () -> reviewManager.getReviewById(reviewId));

        // verify
        verify(reviewRepository, times(1)).findById(reviewId);
    }
}