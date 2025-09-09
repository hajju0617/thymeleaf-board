package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.dto.CommentDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.service.ArticleService;
import com.project.thymeleafboard.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    /*
        댓글 작성

        @Param
        id : article(글) id
        CommentDto : 댓글 내용.
    */
    @PostMapping("/create/{id}")
    public String createComment(Model model
                             , @PathVariable("id") Integer id
                             , @Valid CommentDto commentDto, BindingResult bindingResult) {
        Article article = articleService.getArticle(id);
        if (bindingResult.hasErrors()) {
            // article_detail 뷰템플릿에서 Article 객체가 필요함.
            model.addAttribute("article", article);
            return "article_detail";
        }
        commentService.create(article, commentDto.getContent());
        return "redirect:/article/detail/" + id;
    }
}
