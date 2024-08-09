package com.turkcell.staj.repositories;

import com.turkcell.staj.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
