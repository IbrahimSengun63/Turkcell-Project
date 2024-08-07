package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByOfferId(int offerId);
    List<Review> findByUserId(int userId);
}
