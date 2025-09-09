package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;
}
