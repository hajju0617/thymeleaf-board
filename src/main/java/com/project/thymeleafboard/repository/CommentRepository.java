package com.project.thymeleafboard.repository;

import com.project.thymeleafboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
