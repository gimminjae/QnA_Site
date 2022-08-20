package com.qnasite;

import com.qnasite.question.Question;
import com.qnasite.question.QuestionRepository;
import com.qnasite.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class QnASiteApplicationTests {
    @Autowired
    private QuestionService questionService;

    @Test
    void contextLoads() {
    }
    @Test
    void craeteTestData() {
        for(int i = 0; i < 100; i++) {
            String subject = "제목%d".formatted(i+1);
            String content = "내용%d".formatted(i + 1);
            questionService.create(subject, content);
        }
    }

}
