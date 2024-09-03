package com.epam.user.management.application.repository;

import com.epam.user.management.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    public boolean existsByEmail(String email);

}
