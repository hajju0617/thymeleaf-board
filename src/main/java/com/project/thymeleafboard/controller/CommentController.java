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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.Optional;

import static com.project.thymeleafboard.common.GlobalConst.*;


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
        articleId : article(글) id
        CommentDto : 댓글 내용.
        BindingResult : 데이터 바인딩(Data Binding)과 검증(Validation) 과정에서 발생한 오류 정보를 담아둠. (오류 컨테이너 역할) & 뷰 템플릿에서 오류를 출력할 수 있음.
        Principal : 현재 인증된 사용자(로그인한 사용자) 객체
    */
    @PostMapping("/create/{id}")
    public String createComment(Model model,
                                @PathVariable("id") Integer articleId,
                                @Valid CommentDto commentDto, BindingResult bindingResult,
                                Principal principal) {
        Article article = articleService.getArticle(articleId);
        if (bindingResult.hasErrors()) {
            // article_detail 뷰템플릿에서 Article 객체가 필요함.
            model.addAttribute("article", article);
            Page<Comment> commentList = commentService.getCommentList(article, 0, "date");
            // 'redirect'가 아니므로 model에 수동으로 값을 채워 넣어줘야됨.
            model.addAttribute("commentList", commentList);
            model.addAttribute("page", 0);
            model.addAttribute("size", 10);
            model.addAttribute("sortType", "date");
            return "article_detail";
        }
        Comment comment = commentService.create(article, commentDto.getContent(), userService.findByUsernameOrThrow(principal.getName()));
        int cmtPage = commentService.getPageNumberOfComment(article, comment.getId());
        return String.format("redirect:/article/detail/%s?cmt-page=%s#comment_%s", articleId, cmtPage, comment.getId());
    }

    /*
        댓글 추천 처리 메서드.

        @Param
        commentId : 댓글(comment) id
        principal : Principal : 현재 인증된 사용자(로그인한 사용자) 객체.
    */

    @PostMapping("/vote/{id}")
    @ResponseBody
    public ResponseEntity<Integer> commentVote(@PathVariable("id") Integer commentId, Principal principal) {
        Comment comment = commentService.getComment(commentId);
        SiteUser siteUser = userService.findByUsernameOrThrow(principal.getName());
        commentService.toggleVote(comment, siteUser);

        return ResponseEntity.status(HttpStatus.OK).body(comment.getVoter().size());
    }

    @GetMapping("/modify/{id}")
    public String modifyComment(Model model, @PathVariable(value = "id") Integer commentId, Principal principal) {
        Comment comment = commentService.getComment(commentId);
        commentService.verifyCommentAuthor(comment, principal, comment.getArticle().getId());
        CommentDto commentDto = CommentDto.fromEntity(comment);
        model.addAttribute("commentDto", commentDto);
        return "comment_form";
    }

    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentDto commentDto, BindingResult bindingResult,
                                @PathVariable(value = "id") Integer commentId, Principal principal,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = commentService.getComment(commentId);
        commentService.verifyCommentAuthor(comment, principal, comment.getArticle().getId());
        if (comment.getContent().equals(commentDto.getContent())) {
            bindingResult.reject("noChangeDetected", ERROR_NO_CHANGE_DETECTED);
            return "comment_form";
        }
        commentService.modifyComment(comment, commentDto);
        redirectAttributes.addFlashAttribute("successMsgCmt", SUCCESS_MODIFY);
        int cmtPage = commentService.getPageNumberOfComment(comment.getArticle(), comment.getId());
        return String.format("redirect:/article/detail/%s?cmt-page=%s#comment_%s", comment.getArticle().getId(), cmtPage, comment.getId());
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable(value = "id") Integer commentId, Principal principal, RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getComment(commentId);
        commentService.verifyCommentAuthor(comment, principal, comment.getArticle().getId());
        int targetCmtPage = commentService.getRedirectPageAfterDelete(comment);
        Optional<Integer> previousCmtId = commentService.findPreviousCommentId(comment.getArticle(), comment.getId());
        commentService.deleteComment(commentId);
        redirectAttributes.addFlashAttribute("successMsgCmt", SUCCESS_DELETE);
        if (previousCmtId.isPresent()) {
            return String.format("redirect:/article/detail/%s?cmt-page=%s#comment_%s", comment.getArticle().getId(), targetCmtPage, previousCmtId.get());
        } else {
            return String.format("redirect:/article/detail/%s?cmt-page=%s", comment.getArticle().getId(), targetCmtPage);
        }
    }
}
