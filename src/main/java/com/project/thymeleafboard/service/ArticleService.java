package com.project.thymeleafboard.service;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.exception.DataNotFoundException;
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
            article.validateStatus(article.getStatus());
            return article;
        } else {
            throw new DataNotFoundException("존재하지 않는 게시글 조회");
        }
    }

    public void createArticle(String title, String content) {
        Article article = Article.create(title, content);
        articleRepository.save(article);
    }
}
