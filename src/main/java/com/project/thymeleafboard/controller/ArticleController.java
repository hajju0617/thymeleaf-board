package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.dto.ArticleDto;
import com.project.thymeleafboard.dto.CommentDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.service.ArticleService;
import com.project.thymeleafboard.service.CommentService;
import com.project.thymeleafboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.project.thymeleafboard.common.GlobalConst.ERROR_NO_CHANGE_DETECTED;

@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentService commentService;


    /*
        전체 게시판 글 조회 메서드.

        @Param
        model : 뷰 템플릿에서 사용할 객체를 넘겨주기 위해서.
        page : 클라이언트에서 요청한 페이지 번호.
        size : 페이지당 게시글 수.

    */
    @GetMapping("/list")
    public String articleList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size) {
        articleService.validateArticlePageNum(page);
        articleService.validateArticlePageSize(size);
        Page<Article> articlePage = articleService.getArticleList(page, size);
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("size", size);
        return "article_list";
    }

    /*
        글 상세 조회 메서드.

        @Param
        model : 뷰 템플릿에서 사용할 객체를 넘겨주기 위해서.
        id : article(글) id
        page : 상세 조회 페이지에서 목록으로 되돌아갈 때 필요함.
        CommentDto : article_detail 뷰템플릿에서 CommentDto 객체가 필요함. (th:object)
        Principal : 현재 인증된 사용자(로그인한 사용자) 객체. (뷰 페이지에서 article, comment 추천했는지 유무 판단.)
        commentPage : 글 상세 조회 페이지에서 댓글 페이징처리.
    */
    @GetMapping("/detail/{id}")
    public String articleDetail(Model model, @PathVariable("id") Integer id,
                                @RequestParam(value = "page", defaultValue = "0") int page, CommentDto commentDto
            , Principal principal, @RequestParam(value = "cmt-page", defaultValue = "0") int commentPage) {
        articleService.validateArticlePageNum(page);
        Article article = articleService.getArticleDetail(id);
        commentService.validateCommentPageNumber(article, commentPage, id, page);
        Page<Comment> commentList = commentService.getCommentList(article, commentPage);
        model.addAttribute("article", article);
        model.addAttribute("page", page);
        model.addAttribute("commentPage", commentList);
        // 상세 페이지 추천기능으로 인해 추가.
        if (principal != null) {
            SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
            model.addAttribute("siteUser", siteUser);
        }
        return "article_detail";
    }

    /*
        글쓰기 뷰 메서드.

        @Param
        ArticleDto : article_form 뷰템플릿에서 ArticleDto 객체가 필요함. (th:object)
        model : 글 작성인지 수정인지 구분할 용도로 사용.
    */

    @GetMapping("/create")
    public String createArticle(Model model, ArticleDto articleDto) {
        model.addAttribute("mode", "create");
        return "/article_form";
    }

    /*
        글쓰기 저장 메서드.

        @Param
        ArticleDto : 글 제목, 내용
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
        model : 글 작성인지 수정인지 구분할 용도로 사용.
        principal : 현재 인증된 사용자(로그인한 사용자) 객체.
    */
    @PostMapping("/create")
    public String createArticle(@Valid ArticleDto articleDto, BindingResult bindingResult, Principal principal, Model model) {
        // BindingResult : @Valid 에노테이션의 검증 결과를 담고 있는 객체.
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "/article_form";
        }
        articleService.createArticle(articleDto, userService.findByUsernameOrThrow(principal.getName()));
        return "redirect:/article/list";
    }

    /*
        글을 추천하는 메서드.

        @Param
        id : 해당 글 id
        principal : 현재 인증된 사용자(로그인한 사용자) 객체. (뷰 페이지에서 추천 유무 판단.)
    */

    @PostMapping("/vote/{id}")
    @ResponseBody
    public ResponseEntity<Integer> articleVote(@PathVariable("id") Integer id, Principal principal) {
        Article article = articleService.getArticle(id);
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        articleService.toggleVoteArticle(article, siteUser);

        return ResponseEntity.status(HttpStatus.OK).body(article.getVoter().size());
    }

    /*
        수정 요청시 뷰 페이지 반환 메서드.

        @Param
        model : 뷰 템플릿에서 사용할 객체를 넘겨주기 위해서.
        id : 글(article) id
        principal : Principal : 현재 인증된 사용자(로그인한 사용자) 객체.
    */

    @GetMapping("/modify/{id}")
    public String modifyArticle(Model model,
                                @PathVariable(value = "id") Integer id, Principal principal) {
        Article article = articleService.getArticle(id);
        articleService.verifyArticleAuthor(article, principal, id);
        ArticleDto articleDto = ArticleDto.fromEntity(article);
        model.addAttribute("articleDto", articleDto);
        return "article_form";
    }

    @PostMapping("/modify/{id}")
    public String modifyArticle(@Valid ArticleDto articleDto, BindingResult bindingResult,
                                @PathVariable(value = "id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        Article article = articleService.getArticle(id);
        articleService.verifyArticleAuthor(article, principal, id);
        if ((article.getTitle().equals(articleDto.getTitle()) && article.getContent().equals(articleDto.getContent()))) {
            bindingResult.reject("noChangeDetected", ERROR_NO_CHANGE_DETECTED);
            return "article_form";
        }
        articleService.modifyArticle(article, articleDto);
        return "redirect:/article/detail/" + id;
    }
}
