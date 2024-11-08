package com.sarat.jwt.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.sarat.jwt.model.User;
import com.sarat.jwt.model.UserEntity;

public interface UserRepository extends JpaRepository<User, Integer> {

    UserEntity findByUsername(String username);
}
