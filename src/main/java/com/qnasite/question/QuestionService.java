package com.qnasite.question;

import com.qnasite.DataNotFoundException;
import com.qnasite.answer.Answer;
import com.qnasite.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return questionRepository.findAll();
    }
    public Question getQuestion(long id) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if(optionalQuestion.isPresent()) {
            return optionalQuestion.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser siteUser) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(siteUser);
        questionRepository.save(question);
    }
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> specification = search(kw);
        return questionRepository.findAll(specification, pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        questionRepository.save(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                Join<Question, SiteUser> u1 = root.join("author", JoinType.LEFT);
                Join<Question, Answer> a = root.join("answerList", JoinType.LEFT);
                Join<Answer, Question> u2 = a.join("author", JoinType.LEFT);

                return criteriaBuilder.or(criteriaBuilder.like(root.get("subject"), "%" + kw + "%"), // 제목
                        criteriaBuilder.like(root.get("content"), "%" + kw + "%"),      // 내용
                        criteriaBuilder.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        criteriaBuilder.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        criteriaBuilder.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public void addHit(Question question) {
        question.setHit(question.getHit() + 1);
        questionRepository.save(question);
    }
}