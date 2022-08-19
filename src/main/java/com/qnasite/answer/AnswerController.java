package com.qnasite.answer;

import com.qnasite.question.Question;
import com.qnasite.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") long id, @RequestParam String content) {
        Question question = questionService.getQuestion(id);
        //답변 저장 기능 구현해야 함
        return "redirect:/question/detail/%s".formatted(id);
    }
}
