package com.project.thymeleafboard.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;


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

    @Column(columnDefinition = "integer default 1", nullable = false)
    private int status;

    @ManyToOne
    private Article article;

//    @ManyToOne
//    private SiteUser author;
//
//    @ManyToMany
//    private Set<SiteUser> voter;

    public Comment() {

    }

    public Comment(Article article, String content) {
        this.content = content;
        this.article = article;
        this.createDate = LocalDateTime.now();
        this.status = 1;
    }

    public static Comment createComment(Article article, String content) {
        return new Comment(article, content);
    }

    // 수정 테스트용 임시 메서드.
    public void patchCommentTest(Comment comment) {
        if (comment.getContent() != null) {
            this.content = comment.getContent();
        }
        this.modifyDate = LocalDateTime.now();
    }

}
