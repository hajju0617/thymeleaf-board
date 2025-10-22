package com.project.thymeleafboard.entity;

import com.project.thymeleafboard.dto.ArticleDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private SiteUser author;

    // cascade = CascadeType.REMOVE : 참조 무결성 제약조건을 지키기 위함.
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;


    @Column(columnDefinition = "integer default 0", nullable =  false)
    private int countView;


    @Column(columnDefinition = "integer default 0", nullable = false)
    private int countVote;


    @ManyToMany
    private Set<SiteUser> voter;

    public Article() {
    }

    private Article(String title, String content, SiteUser author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.countView = 0;
        this.createDate = LocalDateTime.now();
    }

    public static Article create(ArticleDto articleDto, SiteUser siteUser) {
        return new Article(articleDto.getTitle(), articleDto.getContent(), siteUser);
    }

    public void incrementCountView() {
        this.countView++;
    }

    public void incrementCountVote() {
        this.countVote++;
    }

    public void decrementCountVote() {
        this.countVote--;
    }

    public void modify(Article article, ArticleDto articleDto) {
        if (!article.getTitle().equals(articleDto.getTitle())) {
            this.title = articleDto.getTitle();
        }
        if (!article.getContent().equals(articleDto.getContent())) {
            this.content = articleDto.getContent();
        }
        // 컨트롤러에서 둘 다 바뀐게 없는지 먼저 검증했으므로 둘 중 하나는 무조건 바뀌었음.
        this.modifyDate = LocalDateTime.now();
    }



    // 테스트코드에서 수정용 임시 메서드.
    public void patchTest(Article article) {
        if (article.getTitle() != null) {
            this.title = article.title;
        }
        if (article.getContent() != null) {
            this.content = article.content;
        }
    }
}
