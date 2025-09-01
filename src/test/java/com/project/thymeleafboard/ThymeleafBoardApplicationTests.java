package com.project.thymeleafboard;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThymeleafBoardApplicationTests {

    @BeforeEach
    void setUp() {
        System.out.println(this);
    }

    @Test
    void test1() {
        System.out.println("테스트1 실행");
    }

    @Test
    void test2() {
        System.out.println("테스트2 실행");
    }


//    @Test
//    void contextLoads() {
//    }
}
