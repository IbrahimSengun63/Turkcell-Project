package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
