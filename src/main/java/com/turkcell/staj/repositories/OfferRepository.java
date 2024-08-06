package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer,Integer> {
}
