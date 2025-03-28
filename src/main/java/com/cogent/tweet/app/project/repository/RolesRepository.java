package com.cogent.tweet.app.project.repository;

import com.cogent.tweet.app.project.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRole(String role);
}
