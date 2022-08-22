package com.qnasite.answer;

import com.qnasite.DataNotFoundException;
import com.qnasite.question.Question;
import com.qnasite.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public Answer getAnswer(Long id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        if(optionalAnswer.isPresent()) {
            return optionalAnswer.get();
        } else throw new DataNotFoundException("answer ont found");
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
}
