package com.project.thymeleafboard.service;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.exception.DataNotFoundException;
import com.project.thymeleafboard.exception.InvalidPageException;
import com.project.thymeleafboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.thymeleafboard.common.GlobalConst.*;

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
            throw new DataNotFoundException(ERROR_COMMENT_NOT_FOUND);
        }
    }

    public Page<Comment> getCommentList(Article article, int page) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, commentPageSize, Sort.by(orderList));
        return commentRepository.findAllByArticle(article, pageable);
    }

    @Transactional
    public void toggleVote(Comment Comment, SiteUser siteUser) {
        if (Comment.getVoter().contains(siteUser)) {
            Comment.getVoter().remove(siteUser);
        } else {
            Comment.getVoter().add(siteUser);
        }
    }

    public void validateCommentPageNumber(Article article, int commentPage, Integer id, int page) {
        isPageNumberNegative(commentPage, id, page);
        isPageOutOfRange(article, commentPage, id, page);
    }
    private void isPageNumberNegative(int commentPage, Integer id, int page) {
        if (commentPage < 0) {
            throw new InvalidPageException(ERROR_NEGATIVE_PAGE_NUMBER, id, page);
        }
    }

    private void isPageOutOfRange(Article article, int commentPage, Integer id, int page) {
        int totalPages = (int) Math.ceil(article.getCommentList().size() / (double) commentPageSize);
        // totalPages > 0 조건을 넣은 이유 : 댓글이 0개인 게시글의 경우 예외가 발생되면 안됨.
        if (totalPages > 0 && commentPage >= totalPages) {
            throw new InvalidPageException(ERROR_PAGE_OUT_OF_COMMENT_RANGE, id, page);
        }
    }
}
