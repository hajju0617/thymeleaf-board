package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.project.thymeleafboard.common.GlobalConst.CONTENT_REQUIRED;

@Getter
@Setter
public class CommentDto {
    @NotBlank(message = CONTENT_REQUIRED)
    private String content;
}
