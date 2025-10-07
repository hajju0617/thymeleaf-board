package com.project.thymeleafboard.service;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.exception.DataNotFoundException;
import com.project.thymeleafboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.project.thymeleafboard.entity.Comment.createComment;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void create(Article article, String content, SiteUser author) {
        Comment comment = Comment.createComment(article, content, author);
        commentRepository.save(comment);
    }

    public Comment getComment(Integer id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            return optionalComment.get();
        } else {
            throw new DataNotFoundException("존재하지 않는 댓글 조회.");
        }
    }

    @Transactional
    public void toggleVote(Comment Comment, SiteUser siteUser) {
        if (Comment.getVoter().contains(siteUser)) {
            Comment.getVoter().remove(siteUser);
        } else {
            Comment.getVoter().add(siteUser);
        }
    }
}
