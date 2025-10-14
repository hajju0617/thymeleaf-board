package com.project.thymeleafboard.entity;

import com.project.thymeleafboard.dto.CommentDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.Set;



@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;


    @ManyToOne
    private Article article;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    private Set<SiteUser> voter;

    public Comment() {
    }

    private Comment(Article article, String content, SiteUser author) {
        this.content = content;
        this.article = article;
        this.author = author;
        this.createDate = LocalDateTime.now();
    }

    public static Comment createComment(Article article, String content, SiteUser author) {
        return new Comment(article, content, author);
    }

    public void modify(CommentDto commentDto) {
        this.content = commentDto.getContent();
        this.modifyDate = LocalDateTime.now();
    }



    // 테스트코드에서 수정용 임시 메서드.
    public void patchCommentTest(Comment comment) {
        if (comment.getContent() != null) {
            this.content = comment.getContent();
        }
        this.modifyDate = LocalDateTime.now();
    }

}
