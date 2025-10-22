package com.project.thymeleafboard.repository;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByArticle(Article article, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.article.id = :articleId AND c.createDate < (SELECT c2.createDate FROM Comment c2 WHERE c2.id = :commentId)")
    long countCommentsBefore(@Param("articleId") Integer articleId, @Param("commentId") Integer commentId);

    @Query("""
        SELECT c.id FROM Comment c
        WHERE c.article.id = :articleId AND (c.createDate < (SELECT d.createDate FROM Comment d WHERE d.id = :CommentId) AND c.id < :CommentId)
        ORDER BY c.createDate DESC, c.id DESC
        LIMIT 1
    """)
    Optional<Integer> findPreviousCommentId(@Param("articleId") Integer articleId, @Param("CommentId") Integer CommentId);
}
