package com.turkcell.staj.repositories;

import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> findByUserIdAndStatus(int id, Status status);
    boolean existsByUserIdAndOfferId(int userId, int offerId);
}
