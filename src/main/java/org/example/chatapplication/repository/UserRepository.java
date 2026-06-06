package org.example.chatapplication.repository;

import org.example.chatapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByUsername(String username);

    Optional<User> findUserByUsername(String username);
}
