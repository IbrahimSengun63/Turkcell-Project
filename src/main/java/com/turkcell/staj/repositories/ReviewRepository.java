package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    //@Query("SELECT r FROM Review r WHERE r.offer.id = :offerId")
    //List<Review> findByOfferId(@Param("offerId") int offerId);
    List<Review> findByOfferId(int offerId);
    List<Review> findByUserId(int userId);
}
