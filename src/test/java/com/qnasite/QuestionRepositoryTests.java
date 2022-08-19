package com.qnasite;

import com.qnasite.question.Question;
import com.qnasite.question.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuestionRepositoryTests {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void testJpa_저장() {
        Question question1 = new Question();
        question1.setSubject("주제1");
        question1.setContent("내용1");
        question1.setCreateData(LocalDateTime.now());
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setSubject("주제2");
        question2.setContent("내용2");
        question2.setCreateData(LocalDateTime.now());
        questionRepository.save(question2);
    }
    @Test
    void testJpa_조회() {
        List<Question> list = questionRepository.findAll();
        assertThat(list.size()).isEqualTo(2);

        Question question = list.get(0);
        assertThat(question.getSubject()).isEqualTo("주제1");
    }
}
