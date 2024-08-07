package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.review.responses.ResponseGetAllOfferReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import com.turkcell.staj.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

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
}
