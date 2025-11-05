package com.project.thymeleafboard.service;

import com.project.thymeleafboard.dto.ArticleDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.exception.DataNotFoundException;
import com.project.thymeleafboard.exception.InvalidPageException;
import com.project.thymeleafboard.exception.InvalidValueException;
import com.project.thymeleafboard.exception.ResourcePermissionDeniedException;
import com.project.thymeleafboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

import static com.project.thymeleafboard.common.GlobalConst.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;


    public Page<Article> getArticleList(int page, int size, String sortType, String searchType, String keyword) {
        List<Sort.Order> orderList = new ArrayList<>();
        switch (sortType) {
            case "vote" -> orderList.add(Sort.Order.desc("countVote"));
            case "view" -> orderList.add(Sort.Order.desc("countView"));
            default -> orderList.add(Sort.Order.desc("createDate"));
        }
        // 추천수 or 조회수가 같은 경우를 대비해서 두 번째 정렬 조건을 추가.
        if (!("date".equals(sortType))) {
            orderList.add(Sort.Order.desc("createDate"));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));
        if (keyword.trim().isEmpty()) {
            Page<Article> articlePage = articleRepository.findAll(pageable);
            if (articlePage.getContent().isEmpty()) {
                log.warn("size : {}, page : {}", size, page);
                throw new InvalidPageException(ERROR_PAGE_OUT_OF_ARTICLE_RANGE);
            }
            return articlePage;
        } else {
            return articleRepository.findAllByKeyword(keyword, searchType, pageable);
        }
    }


    public Article getArticle(Integer id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (optionalArticle.isPresent()) {
            return optionalArticle.get();
        } else {
            throw new DataNotFoundException(ERROR_ARTICLE_NOT_FOUND);
        }
    }

    @Transactional
    public Article getArticleDetail(Integer id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.incrementCountView();
            return article;
        } else {
            throw new DataNotFoundException(ERROR_ARTICLE_NOT_FOUND);
        }
    }

    @Transactional
    public void createArticle(ArticleDto articleDto, SiteUser siteUser) {
        Article article = Article.create(articleDto, siteUser);
        articleRepository.save(article);
    }

    @Transactional
    public void toggleVoteArticle(Article article, SiteUser siteUser) {
        validateNotSelfVote(article, siteUser);
        if (article.getVoter().contains(siteUser)) {
            article.decrementCountVote();
            article.getVoter().remove(siteUser);
        } else {
            article.incrementCountVote();
            article.getVoter().add(siteUser);
        }
    }

    private void validateNotSelfVote(Article article, SiteUser siteUser) {
        if (article.getAuthor().getUsername().equals(siteUser.getUsername())) {
            throw new ResourcePermissionDeniedException(ERROR_SELF_VOTE, article.getId());
        }
    }

    @Transactional
    public void modifyArticle(Article article, ArticleDto articleDto) {
        article.modify(article, articleDto);
    }

    @Transactional
    public void deleteArticle(Integer id) {
        articleRepository.deleteById(id);
    }

    public void validateArticleListParams(int page, int size, String sortType, String searchType) {
        validateArticlePageNum(page);
        validateArticlePageSize(size);
        validateArticlePageSort(sortType);
        validateArticleSearchType(searchType);
    }

    public void validateArticleDetailParams(int page, int size, String sortType) {
        validateArticlePageNum(page);
        validateArticlePageSize(size);
        validateArticlePageSort(sortType);
    }

    private void validateArticlePageNum(int page) {
        if (page < 0) {
            throw new InvalidPageException(ERROR_NEGATIVE_PAGE_NUMBER);
        }
    }

    private void validateArticlePageSize(int size) {
        final Set<Integer> ALLOWED_SIZES = Set.of(10, 20, 40, 50);
        if (!ALLOWED_SIZES.contains(size)) {
            throw new InvalidValueException(ERROR_INVALID_LIST_SIZE);
        }
    }

    private void validateArticlePageSort(String sortType) {
        final Set<String> sorts = Set.of("vote", "view", "date");
        if (!sorts.contains(sortType)) {
            throw new InvalidValueException(ERROR_INVALID_SORT_TYPE);
        }
    }

    private void validateArticleSearchType(String searchType) {
        final Set<String> sorts = Set.of("title_content", "title", "author", "comment");
        if (!sorts.contains(searchType)) {
            throw new InvalidValueException(ERROR_SEARCH_OPTION);
        }
    }

    public void verifyArticleAuthor(Article article, Principal principal, Integer id) {
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResourcePermissionDeniedException(ERROR_AUTHOR_MISMATCH, id);
        }
    }
}



