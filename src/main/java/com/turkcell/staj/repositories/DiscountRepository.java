package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    Optional<Discount> findByOfferId(int offerId);
}
