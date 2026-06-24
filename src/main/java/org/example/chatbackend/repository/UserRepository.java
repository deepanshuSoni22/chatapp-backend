package org.example.chatbackend.repository;

import org.example.chatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByUsername(String username);

    Optional<User> findUserByUsername(String username);
}
