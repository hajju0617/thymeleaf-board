package com.project.thymeleafboard.repository;

import com.project.thymeleafboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByTitleAndContent(String title, String content);

    List<Board> findByTitleLike(String title);
}
