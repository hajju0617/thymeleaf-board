package com.project.thymeleafboard.service;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.thymeleafboard.entity.Comment.createComment;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void create(Article article, String content) {
        Comment comment = Comment.createComment(article, content);
        commentRepository.save(comment);
    }
}
