package com.project.thymeleafboard.dto;

import com.project.thymeleafboard.entity.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.project.thymeleafboard.common.GlobalConst.CONTENT_REQUIRED;
import static com.project.thymeleafboard.common.GlobalConst.TITLE_REQUIRED;

@Getter
@Setter
public class ArticleDto {
    @NotBlank(message = TITLE_REQUIRED)
    @Size(max = 100)
    private String title;

    @NotBlank(message = CONTENT_REQUIRED)
    private String content;

    public ArticleDto() {

    }

    private ArticleDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(article.getTitle(), article.getContent());
    }
}
