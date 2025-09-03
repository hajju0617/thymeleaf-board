package com.project.thymeleafboard;

import com.project.thymeleafboard.entity.Article;
import com.project.thymeleafboard.repository.ArticleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class ArticleTest {
    @Autowired
    private ArticleRepository articleRepository;

    private static JdbcTemplate staticJdbcTemplate;


    @Autowired
    public void setStaticJdbcTemplate(JdbcTemplate jdbcTemplate) {
        // @Autowired는 static 필드에 직접 주입되지 않으므로, setter를 통해 주입받음
        staticJdbcTemplate = jdbcTemplate;
    }

    @AfterAll
    static void cleanupDatabase() {
        // 모든 테스트가 끝난 후 딱 한 번만 실행
        System.out.println("모든 테스트가 완료되었음. 데이터베이스를 초기화.");
        // 데이터 삭제
        staticJdbcTemplate.execute("DELETE FROM article");
        // AUTO_INCREMENT 초기화
        staticJdbcTemplate.execute("ALTER TABLE article AUTO_INCREMENT = 1");
    }

    private Article article1;
    private Article article2;
    private Article article3;

    @BeforeEach
    void setupCommonData() {
        // 모든 테스트에 공통으로 사용될 데이터를 한 번만 미리 생성
        article1 = articleRepository.save(new Article("테스트 제목1", "테스트 내용1"));
        article2 = articleRepository.save(new Article("테스트 제목2", "테스트 내용2"));
        article3 = articleRepository.save(new Article("테스트 제목3", "테스트 내용3"));
    }

    @Test
    @DisplayName("글 작성 테스트")
    void createTest() {
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
    }

    @Test
    @DisplayName("전체글 조회 테스트")
    void findTest() {
        List<Article> articleList = articleRepository.findAll();
        Assertions.assertEquals(3, articleList.size());

        Article article = articleList.get(0);
        Assertions.assertEquals("테스트 제목1", article.getTitle());
    }

    @Test
    @DisplayName("단일 글 조회 테스트")
    void find2Test() {
        Optional<Article> optionalArticle = articleRepository.findById(1);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            Assertions.assertEquals("테스트 제목1", article.getTitle());
            Assertions.assertEquals("테스트 내용1", article.getContent());
        }
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 테스트")
    void find3Test() {
        Optional<Article> optionalArticle = articleRepository.findById(100);

        Assertions.assertTrue(optionalArticle.isEmpty());
    }

    @Test
    @DisplayName("제목, 내용으로 조회 테스트")
    void findTest4() {
        Article article = articleRepository.findByTitleAndContent("테스트 제목1", "테스트 내용1");
        Assertions.assertEquals("테스트 제목1", article.getTitle());
        Assertions.assertEquals("테스트 내용1", article.getContent());
    }

    @Test
    @DisplayName("특정 문자열을 포함하는 데이터 조회 데스트")
    void findTest5() {
        List<Article> byTitleLike = articleRepository.findByTitleLike("%테스%");
        Assertions.assertEquals(3, byTitleLike.size());
        Assertions.assertEquals("테스트 제목2", byTitleLike.get(1).getTitle());
    }

    @Test
    @DisplayName("수정 테스트")
    void patchTest() {
        Article updateArticle = new Article("수정된 테스트 제목1", "수정된 테스트 내용1");

        article1.patchTest(updateArticle);
        article1 = articleRepository.save(article1);
        Assertions.assertEquals("수정된 테스트 제목1", article1.getTitle());
        Assertions.assertEquals("수정된 테스트 내용1", article1.getContent());
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteTest() {
        Optional<Article> optionalArticle = articleRepository.findById(article1.getId());

        if (optionalArticle.isPresent()) {
            articleRepository.delete(optionalArticle.get());
        }
        Assertions.assertEquals(2, articleRepository.count());
    }
}
