package com.project.thymeleafboard.controller;

import com.project.thymeleafboard.dto.CommentDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.service.ArticleService;
import com.project.thymeleafboard.service.CommentService;
import com.project.thymeleafboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.project.thymeleafboard.common.GlobalConst.ERROR_NO_CHANGE_DETECTED;


@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final UserService userService;

    /*
        댓글 작성

        @Param
        id : article(글) id
        CommentDto : 댓글 내용.
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
        Principal : 현재 인증된 사용자(로그인한 사용자) 객체
    */
    @PostMapping("/create/{id}")
    public String createComment(Model model,
                                @PathVariable("id") Integer id,
                                @Valid CommentDto commentDto, BindingResult bindingResult,
                                Principal principal) {
        Article article = articleService.getArticle(id);
        if (bindingResult.hasErrors()) {
            // article_detail 뷰템플릿에서 Article 객체가 필요함.
            model.addAttribute("article", article);
            return "article_detail";
        }
        commentService.create(article, commentDto.getContent(), userService.findByUsernameOrThrow(principal.getName()));
        return "redirect:/article/detail/" + id;
    }

    /*
        댓글 추천 처리 메서드.

        @Param
        id : 댓글(comment) id
        principal : Principal : 현재 인증된 사용자(로그인한 사용자) 객체.
    */

    @PostMapping("/vote/{id}")
    @ResponseBody
    public ResponseEntity<Integer> commentVote(@PathVariable("id") Integer id, Principal principal) {
        Comment comment = commentService.getComment(id);
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        commentService.toggleVote(comment, siteUser);

        return ResponseEntity.status(HttpStatus.OK).body(comment.getVoter().size());
    }

    @GetMapping("/modify/{id}")
    public String modifyComment(Model model, @PathVariable(value = "id") Integer id, Principal principal) {
        Comment comment = commentService.getComment(id);
        commentService.verifyCommentAuthor(comment, principal);
        CommentDto commentDto = CommentDto.fromEntity(comment);
        model.addAttribute("commentDto", commentDto);
        return "comment_form";
    }

    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentDto commentDto, BindingResult bindingResult,
                                @PathVariable(value = "id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = commentService.getComment(id);
        commentService.verifyCommentAuthor(comment, principal);
        if (comment.getContent().equals(commentDto.getContent())) {
            bindingResult.reject("noChangeDetected", ERROR_NO_CHANGE_DETECTED);
            return "comment_form";
        }
        commentService.modifyComment(comment, commentDto);
        return "redirect:/article/detail/" + comment.getArticle().getId();
    }
}
