package com.project.thymeleafboard;

import com.project.thymeleafboard.dto.ArticleDto;
import com.project.thymeleafboard.dto.CommentDto;
import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.repository.ArticleRepository;
import com.project.thymeleafboard.repository.CommentRepository;
import com.project.thymeleafboard.service.ArticleService;
import com.project.thymeleafboard.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PageTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

//    @Test
//    void createData() {
//        for (int i = 0; i < 100; i++) {
//            ArticleDto articleDto = new ArticleDto();
//            articleDto.setTitle(String.format("임시 데이터 : %05d" ,i));
//            articleDto.setContent("임시 데이터");
//            articleRepository.save(Article.create(articleDto, null));
//        }
//    }

    @Test
    void createCommentData() {
        for (int i = 0; i < 200; i++) {
            Article article = articleService.getArticle(508);
            CommentDto commentDto = new CommentDto();
            commentDto.setContent("임시 데이터");
            commentRepository.save(Comment.createComment(article, commentDto.getContent(), userService.findByUsernameOrThrow("zxc123")));
        }
    }
}
