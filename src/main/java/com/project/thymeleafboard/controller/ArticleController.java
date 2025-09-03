package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {
    private final ArticleService articleService;

    /*
        전체 게시판 글 조회.
    */
    @GetMapping("/list")
    public String list(Model model) {
        List<Article> articleList = articleService.getList();
        model.addAttribute("articleList", articleList);
        return "article_list";
    }

    /*
        상세 조회 메서드

        @Param
        id : 글 id
    */
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "article_detail";
    }
}
