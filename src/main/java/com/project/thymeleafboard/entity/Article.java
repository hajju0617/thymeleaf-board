package com.project.thymeleafboard.entity;

import com.project.thymeleafboard.exception.DeletedArticleException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.thymeleafboard.common.GlobalConst.SUCCESS;

@Entity
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Column(columnDefinition = "integer default 1", nullable = false)
    private int status;

    // cascade = CascadeType.REMOVE : 참고 무결성 제약조건을 지키기 위함.
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;


//    @Column(columnDefinition = "integer default 0", nullable =  false)
//    private int countView;
//
//    @ManyToOne
//    private SiteUser author;
//
//    @ManyToMany
//    private Set<SiteUser> voter;

    public Article() {

    }

    private Article(String title, String content) {
        this.title = title;
        this.content = content;
        this.createDate = LocalDateTime.now();
        this.status = SUCCESS;
    }

    public static Article create(String title, String content) {
        return new Article(title, content);
    }

    public void validateStatus(int status) {
        if (status == -1) {
            throw new DeletedArticleException("삭제된 게시글 조회");
        }
    }


    // 수정 테스트용 임시 메서드.
    public void patchTest(Article article) {
        if (article.getTitle() != null) {
            this.title = article.title;
        }
        if (article.getContent() != null) {
            this.content = article.content;
        }
    }
}
