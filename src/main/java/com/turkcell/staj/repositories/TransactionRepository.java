package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> findByUserId(int id);
    boolean existsByUserIdAndOfferId(int userId, int offerId);
}
