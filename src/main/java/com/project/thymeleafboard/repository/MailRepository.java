package com.project.thymeleafboard.repository;

import com.project.thymeleafboard.entity.Mail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MailRepository extends JpaRepository<Mail, Integer> {
    Optional<Mail> findByEmailAndAuthNum(String email, String authNum);

    Optional<Mail> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Mail m WHERE m.createDate < :timeLimit")
    void deleteExpiredAuthNums(@Param("timeLimit") LocalDateTime timeLimit);
}

