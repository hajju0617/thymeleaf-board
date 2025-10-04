package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.dto.ArticleDto;
import com.project.thymeleafboard.dto.CommentDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.service.ArticleService;
import com.project.thymeleafboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

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
        page : 상세 조회 페이지에서 목록으로 되돌아갈 때 필요함.
    */
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "0") int page, CommentDto commentDto) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("page", page);
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
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
    */

    @PostMapping("/create")
    public String createArticle(@Valid ArticleDto articleDto, BindingResult bindingResult, Principal principal) {
        // BindingResult : @Valid 에노테이션의 검증 결과를 담고 있는 객체.
        if (bindingResult.hasErrors()) {
            return "/article_form";
        }
        articleService.createArticle(articleDto, userService.findByUsernameOrThrow(principal.getName()));
        return "redirect:/article/list";
    }
}
