package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.*;
import com.turkcell.staj.entities.Review;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "id", target = "reviewId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "user.id", target = "userId")
    ResponseGetAllOfferReviewDTO reviewToResponseGetAllOfferReviewDto(Review review);

    List<ResponseGetAllOfferReviewDTO> reviewsToResponseGetAllOfferReviewsDto(List<Review> reviews);

    @Mapping(source = "id", target = "reviewId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "user.id", target = "userId")
    ResponseGetAllUserReviewDTO reviewToResponseGetAllUserReviewDto(Review review);

    List<ResponseGetAllUserReviewDTO> reviewsToResponseGetAllUserReviewsDto(List<Review> reviews);

    @Mapping(source = "id", target = "reviewId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "user.id", target = "userId")
    ResponseAddReviewDTO reviewToResponseAddReviewDTO(Review review);

    @Mapping(source = "offerId", target = "offer.id")
    @Mapping(source = "userId", target = "user.id")
    Review requestAddReviewDtoToReview(RequestAddReviewDTO request);

    @Mapping(source = "id", target = "reviewId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "user.id", target = "userId")
    ResponseUpdateReviewDTO reviewToResponseUpdateReviewDTO(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReviewFromRequestUpdateReviewDto(RequestUpdateReviewDTO requestUpdateReviewDTO, @MappingTarget Review review);

    @Mapping(source = "id", target = "reviewId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "user.id", target = "userId")
    ResponseGetReviewDTO reviewToResponseGetReviewDto(Review review);
}
