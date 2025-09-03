package com.project.thymeleafboard.repository;

import com.project.thymeleafboard.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("SELECT a FROM Article a WHERE a.status = 1")
    List<Article> findAllActiveArticle();

    Article findByTitleAndContent(String title, String content);

    List<Article> findByTitleLike(String title);
}
