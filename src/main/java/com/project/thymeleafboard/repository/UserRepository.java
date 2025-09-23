package com.project.thymeleafboard.repository;

import com.project.thymeleafboard.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, Integer> {
    Optional<SiteUser> findByEmail(String email);

    Optional<SiteUser> findByUsername(String username);
}
