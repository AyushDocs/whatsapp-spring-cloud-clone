package com.whatsapp.profile_service.repositories;

import java.util.Optional;

import com.whatsapp.profile_service.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findAllByEmailContainingOrNameContaining(String email, String name, Pageable request);
}