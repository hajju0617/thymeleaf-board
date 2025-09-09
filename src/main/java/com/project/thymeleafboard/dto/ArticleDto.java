package com.project.thymeleafboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDto {
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;
}
