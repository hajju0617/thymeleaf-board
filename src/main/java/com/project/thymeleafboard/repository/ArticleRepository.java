package com.project.thymeleafboard.repository;

import com.project.thymeleafboard.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("select distinct a from Article a " +
            "join a.author u " +
            "left join a.commentList c " +
            "where " +
            "(:searchType = 'title_content' and (a.title like %:keyword% or a.content like %:keyword%)) " +
            "or " +
            "(:searchType = 'title' and a.title like %:keyword%) " +
            "or " +
            "(:searchType = 'author' and u.username like %:keyword%) " +
            "or " +
            "(:searchType = 'comment' and c.content like %:keyword%)")
    Page<Article> findAllByKeyword(@Param("keyword") String keyword,
                                   @Param("searchType") String searchType,
                                   Pageable pageable);


    Article findByTitleAndContent(String title, String content);

    List<Article> findByTitleLike(String title);
}
