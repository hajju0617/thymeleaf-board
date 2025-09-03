package com.project.thymeleafboard.service;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.exception.DataNotFoundException;
import com.project.thymeleafboard.exception.DeletedArticleException;
import com.project.thymeleafboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> getList() {
        return articleRepository.findAllActiveArticle();
    }

    public Article getArticle(Integer id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            if (article.getStatus() == -1) {
                throw new DeletedArticleException("삭제된 게시글 조회");
            }
            return article;
        } else {
            throw new DataNotFoundException("존재하지 않는 게시글 조회");
        }
    }
}
