package com.project.thymeleafboard.dto;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.project.thymeleafboard.common.GlobalConst.CONTENT_REQUIRED;

@Getter
@Setter
public class CommentDto {
    @NotBlank(message = CONTENT_REQUIRED)
    private String content;


    public CommentDto() {
    }

    private CommentDto(String content) {
        this.content = content;
    }
    public static CommentDto fromEntity(Comment comment) {
        return new CommentDto(comment.getContent());
    }
}
