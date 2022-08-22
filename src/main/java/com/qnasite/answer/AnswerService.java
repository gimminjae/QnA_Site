package com.qnasite.answer;

import com.qnasite.question.Question;
import com.qnasite.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question, String content, SiteUser siteuser) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setAuthor(siteuser);
        answer.setQuestion(question);
        answerRepository.save(answer);
    }
}
