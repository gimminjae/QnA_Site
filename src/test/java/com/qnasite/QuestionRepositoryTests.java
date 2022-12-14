package com.qnasite;

import com.qnasite.answer.Answer;
import com.qnasite.answer.AnswerRepository;
import com.qnasite.question.Question;
import com.qnasite.question.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class QuestionRepositoryTests {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void testJpa_저장() {
        Question question1 = new Question();
        question1.setSubject("주제1");
        question1.setContent("내용1");
        question1.setCreateDate(LocalDateTime.now());
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setSubject("주제2");
        question2.setContent("내용2");
        question2.setCreateDate(LocalDateTime.now());
        questionRepository.save(question2);
    }
    @Test
    void testJpa_조회() {
        List<Question> list = questionRepository.findAll();
        assertThat(list.size()).isEqualTo(2);

        Question question = list.get(0);
        assertThat(question.getSubject()).isEqualTo("주제1");
    }
    @Test
    void testJpa_조회2() {
        Optional<Question> optionalQuestion = questionRepository.findById(1L);
        if(optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            assertThat(question.getSubject()).isEqualTo("주제1");
        }
    }
    @Test
    void testJpa_조회3() {
        Question question = questionRepository.findBySubject("주제1");
        assertThat(question.getId()).isEqualTo(1);
    }
    @Test
    void testJpa_조회4() {
        Question question = questionRepository.findBySubjectAndContent("주제1", "내용1");
        assertThat(question.getId()).isEqualTo(1);
    }
    @Test
    void testJpa_조회5() {
        List<Question> questionList = questionRepository.findBySubjectLike("주제%");
        assertThat(questionList.size()).isEqualTo(2);
    }
    @Test
    void testJpa_수정() {
        Optional<Question> optionalQuestion = questionRepository.findById(1L);
        assertTrue(optionalQuestion.isPresent());

        Question question = optionalQuestion.get();
        question.setSubject("수정된 제목");
        questionRepository.save(question);
        assertThat(question.getSubject()).isEqualTo("수정된 제목");
    }
    @Test
    void testJpa_삭제() {
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(2L);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }

    //답변 테스트
    @Test
    void testJpa_답변_저장() {
        Optional<Question> optionalQuestion = questionRepository.findById(1L);
        assertTrue(optionalQuestion.isPresent());
        Question question = optionalQuestion.get();

        Answer answer = new Answer();
        answer.setContent("답변1");
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answerRepository.save(answer);
    }
    @Test
    void testJpa_답변에_연결된_질문_찾기() {
        Optional<Answer> optionalAnswer = answerRepository.findById(1L);
        assertTrue(optionalAnswer.isPresent());

        Answer answer = optionalAnswer.get();
        assertThat(answer.getQuestion().getId()).isEqualTo(1);
    }
    @Transactional //메서드가 종료될 때까지 DB 세션이 유지된다.
    @Test
    void testJpa_질문에_달린_답변_찾기() {
        Optional<Question> oq = this.questionRepository.findById(1L);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("답변1", answerList.get(0).getContent());
    }
}
