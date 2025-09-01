package com.project.thymeleafboard;

import com.project.thymeleafboard.entity.Board;
import com.project.thymeleafboard.repository.BoardRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class BoardTest {
    @Autowired
    private BoardRepository boardRepository;

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
        // AUTO_INCREMENT 초기화
        staticJdbcTemplate.execute("ALTER TABLE board AUTO_INCREMENT = 1");
    }

    private Board board1;
    private Board board2;
    private Board board3;

    @BeforeEach
    void setupCommonData() {
        // 모든 테스트에 공통으로 사용될 데이터를 한 번만 미리 생성
        board1 = boardRepository.save(new Board("테스트 제목1", "테스트 내용1"));
        board2 = boardRepository.save(new Board("테스트 제목2", "테스트 내용2"));
        board3 = boardRepository.save(new Board("테스트 제목3", "테스트 내용3"));
    }

    @Test
    @DisplayName("글 작성 테스트")
    void createTest() {
        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);
    }

    @Test
    @DisplayName("전체글 조회 테스트")
    void findTest() {
        List<Board> boardList = boardRepository.findAll();
        Assertions.assertEquals(3, boardList.size());

        Board board = boardList.get(0);
        Assertions.assertEquals("테스트 제목1", board.getTitle());
    }

    @Test
    @DisplayName("단일 글 조회 테스트")
    void find2Test() {
        Optional<Board> optionalBoard = boardRepository.findById(1);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            Assertions.assertEquals("테스트 제목1", board.getTitle());
            Assertions.assertEquals("테스트 내용1", board.getContent());
        }
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 테스트")
    void find3Test() {
        Optional<Board> optionalBoard = boardRepository.findById(100);

        Assertions.assertTrue(optionalBoard.isEmpty());
    }

    @Test
    @DisplayName("제목, 내용으로 조회 테스트")
    void findTest4() {
        Board board = boardRepository.findByTitleAndContent("테스트 제목1", "테스트 내용1");
        Assertions.assertEquals("테스트 제목1", board.getTitle());
        Assertions.assertEquals("테스트 내용1", board.getContent());
    }

    @Test
    @DisplayName("특정 문자열을 포함하는 데이터 조회 데스트")
    void findTest5() {
        List<Board> byTitleLike = boardRepository.findByTitleLike("%테스%");
        Assertions.assertEquals(3, byTitleLike.size());
        Assertions.assertEquals("테스트 제목2", byTitleLike.get(1).getTitle());
    }

    @Test
    @DisplayName("수정 테스트")
    void patchTest() {
        Board updateBoard = new Board("수정된 테스트 제목1", "수정된 테스트 내용1");

        board1.patchTest(updateBoard);
        board1 = boardRepository.save(board1);
        Assertions.assertEquals("수정된 테스트 제목1", board1.getTitle());
        Assertions.assertEquals("수정된 테스트 내용1", board1.getContent());
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteTest() {
        Optional<Board> optionalBoard = boardRepository.findById(board1.getId());

        if (optionalBoard.isPresent()) {
            boardRepository.delete(optionalBoard.get());
        }
        Assertions.assertEquals(2, boardRepository.count());
    }
}
