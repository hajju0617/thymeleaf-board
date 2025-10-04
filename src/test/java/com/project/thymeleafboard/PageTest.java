package com.project.thymeleafboard;

import com.project.thymeleafboard.dto.ArticleDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PageTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void CreateData() {
        for (int i = 0; i < 100; i++) {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setTitle(String.format("임시 데이터 : %05d" ,i));
            articleDto.setContent("임시 데이터");
            articleRepository.save(Article.create(articleDto, null));
        }
    }
}
