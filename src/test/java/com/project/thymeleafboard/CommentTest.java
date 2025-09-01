package com.project.thymeleafboard;

import com.project.thymeleafboard.entity.Board;
import com.project.thymeleafboard.entity.Comment;
import com.project.thymeleafboard.repository.BoardRepository;
import com.project.thymeleafboard.repository.CommentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CommentTest {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;

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
        staticJdbcTemplate.execute("DELETE FROM board");
        staticJdbcTemplate.execute("DELETE FROM comment");
        // AUTO_INCREMENT 초기화
        staticJdbcTemplate.execute("ALTER TABLE board AUTO_INCREMENT = 1");
        staticJdbcTemplate.execute("ALTER TABLE comment AUTO_INCREMENT = 1");
    }

    private Board board1;
    private Board board2;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;

    @BeforeEach
    void setupCommonData() {
        // 모든 테스트에 공통으로 사용될 데이터를 한 번만 미리 생성
        board1 = boardRepository.save(new Board("테스트 제목1", "테스트 내용1"));
        board2 = boardRepository.save(new Board("테스트 제목2", "테스트 내용2"));
        comment1 = commentRepository.save(new Comment("테스트 댓글1", board1));
        comment2 = commentRepository.save(new Comment("테스트 댓글2", board1));
    }


    @Test
    @DisplayName("댓글 저장 테스트")
    void createTest() {
        comment3 = commentRepository.save(new Comment("테스트 댓글3", board2));
        Assertions.assertEquals("테스트 댓글3", comment3.getContent());
        Assertions.assertEquals("테스트 제목2", comment3.getBoard().getTitle());
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    void findTest() {
        List<Comment> commentList = commentRepository.findAll();
        Assertions.assertEquals(2, commentList.size());

        Comment comment = commentList.get(0);
        Assertions.assertEquals("테스트 제목1", comment.getBoard().getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 댓글 조회 테스트")
    void findTest2() {
        Optional<Board> optionalBoard = boardRepository.findById(2);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            Assertions.assertEquals(0, board.getCommentList().size());
        }
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void patchTest() {
        comment3 = commentRepository.save(new Comment("테스트 댓글3", board2));
        Optional<Comment> optionalComment = commentRepository.findById(comment2.getId());

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.patchCommentTest(comment3);
            commentRepository.save(comment);
        }
        Assertions.assertEquals("테스트 댓글3", comment2.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteTest() {
        Optional<Comment> optionalComment = commentRepository.findById(comment1.getId());

        if (optionalComment.isPresent()) {
            commentRepository.delete(optionalComment.get());
        }
        Assertions.assertEquals(1, commentRepository.count());
    }
}
