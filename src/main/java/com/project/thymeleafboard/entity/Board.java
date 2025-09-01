package com.project.thymeleafboard.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    // cascade = CascadeType.REMOVE : 참고 무결성 제약조건을 지키기 위함.
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

//    @Column(columnDefinition = "integer default 0", nullable =  false)
//    private int countView;
//
//    @ManyToOne
//    private SiteUser author;
//
//    @ManyToMany
//    private Set<SiteUser> voter;

    public Board() {

    }

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
        this.createDate = LocalDateTime.now();
    }


    // 수정 테스트용 임시 메서드.
    public void patchTest(Board board) {
        if (board.getTitle() != null) {
            this.title = board.title;
        }
        if (board.getContent() != null) {
            this.content = board.content;
        }
    }
}
