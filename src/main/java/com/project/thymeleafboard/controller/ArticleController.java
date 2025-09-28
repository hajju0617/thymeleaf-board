package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.dto.ArticleDto;
import com.project.thymeleafboard.dto.CommentDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {
    private final ArticleService articleService;

    /*
        전체 게시판 글 조회.

        @Param
        page : 클라이언트에서 요청한 페이지 번호.
        size : 페이지당 게시글 수.

    */
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page
                                  , @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Article> articlePage = articleService.getList(page, size);
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("size", size);
        return "article_list";
    }

    /*
        글 상세 조회

        @Param
        id : article(글) id
        CommentDto : article_detail 뷰템플릿에서 CommentDto 객체가 필요함. (th:object)
    */
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, CommentDto commentDto) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "article_detail";
    }

    /*
        글쓰기 뷰 메서드.

        @Param
        ArticleDto : article_form 뷰템플릿에서 ArticleDto 객체가 필요함. (th:object)
    */
    @GetMapping("/create")
    public String createArticle(ArticleDto articleDto) {
        return "/article_form";
    }

    /*
        글쓰기 저장 메서드.

        @Param
        ArticleDto : 글 제목, 내용
        BindingResult : @Valid 에노테이션의 검증 결과를 담고 있는 객체. (뷰템플릿에서 검증결과 에러를 출력)
    */
    @PostMapping("/create")
    public String createArticle(@Valid ArticleDto articleDto, BindingResult bindingResult) {
        // BindingResult : @Valid 에노테이션의 검증 결과를 담고 있는 객체.
        if (bindingResult.hasErrors()) {
            return "/article_form";
        }
        articleService.createArticle(articleDto);
        return "redirect:/article/list";
    }
}
