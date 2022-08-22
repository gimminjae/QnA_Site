package com.qnasite.answer;

import com.qnasite.question.Question;
import com.qnasite.question.QuestionForm;
import com.qnasite.question.QuestionService;
import com.qnasite.user.SiteUser;
import com.qnasite.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createAnswer(Model model, @PathVariable("id") long id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        Question question = questionService.getQuestion(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        if(bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        answerService.create(question, answerForm.getContent(), siteUser);
        return "redirect:/question/detail/%s".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyAnswer(AnswerForm answerForm, @PathVariable("id") Long id, Principal principal) {
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyAnswer(@Valid AnswerForm answerForm, BindingResult bindingResult,
                                 @PathVariable("id") Long id, Principal principal) {
        if(bindingResult.hasErrors()) return "answer_form";

        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerService.modify(answer, answerForm.getContent());
        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteAnswer(@PathVariable("id") Long id, Principal principal) {
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        answerService.delete(answer);
        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }
}
