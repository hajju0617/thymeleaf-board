package com.project.thymeleafboard.entity;

import jakarta.persistence.*;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToOne
    private Board board;

//    @ManyToOne
//    private SiteUser author;
//
//    @ManyToMany
//    private Set<SiteUser> voter;

    public Comment() {

    }

    public Comment(String content, Board board) {
        this.content = content;
        this.board = board;
        this.createDate = LocalDateTime.now();
    }

    // 수정 테스트용 임시 메서드.
    public void patchCommentTest(Comment comment) {
        if (comment.getContent() != null) {
            this.content = comment.getContent();
        }
        this.modifyDate = LocalDateTime.now();
    }

}
