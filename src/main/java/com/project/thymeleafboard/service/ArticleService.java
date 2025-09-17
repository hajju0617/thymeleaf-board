package com.project.thymeleafboard.service;

import com.project.thymeleafboard.dto.ArticleDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.exception.DataNotFoundException;
import com.project.thymeleafboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Page<Article> getList(int page, int size) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));
        Page<Article> articlePage = articleRepository.findAllActiveArticle(pageable);
        if (articlePage.getContent().isEmpty()) {
            log.info("size : {}, page : {}", size, page);
            throw new DataNotFoundException("존재하지 않는 페이지 요청.");
        }
        return articlePage;
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

    public void createArticle(ArticleDto articleDto) {
        Article article = Article.create(articleDto);
        articleRepository.save(article);
    }
}


