package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.service.ArticleService;
import com.project.thymeleafboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @PostMapping("/create/{id}")
    public String createComment(Model model
                             , @PathVariable("id") Integer id
                             , @RequestParam(value = "content")String content) {
        Article article = articleService.getArticle(id);
        commentService.create(article, content);
        return "redirect:/article/detail/" + id;
    }
}
