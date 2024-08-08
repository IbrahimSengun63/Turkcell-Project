package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserId(int userId);
    User findByNameAndSurname(String name, String surname);
    List<User> findByBalanceGreaterThan(double balance);
}
