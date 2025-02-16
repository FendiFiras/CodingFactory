package tn.esprit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Quiz;
import tn.esprit.entities.QuizAnswer;
import tn.esprit.entities.QuizQuestion;
import tn.esprit.services.ServiceQuiz;

import java.util.List;

@RestController
@RequestMapping("/Quizs")
public class QuizController {

@Autowired
    private ServiceQuiz serviceQuiz;


    @PostMapping("/add_quiz")
    public void addQuiz(@RequestBody Quiz quiz) {
        serviceQuiz.addQuiz(quiz);
    }

    @GetMapping("/GetAllquizs")
    public List<Quiz> getAllQuizzes() {
        return serviceQuiz.getAllQuizzes();
    }

    @GetMapping("/getQuiz/{id}")
    public Quiz getQuizById(@PathVariable("id") Long quizId) {
        return serviceQuiz.getQuizById(quizId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteQuiz(@PathVariable("id") Long quizId) {
        serviceQuiz.deleteQuiz(quizId);


    }

    @PutMapping("/update_Quiz")
    public Quiz updateQuiz(@RequestBody Quiz quiz) {
        return serviceQuiz.updateQuiz(quiz);
    }




    @PostMapping("/quiz/{quizId}/addQuestion")
    public void addQuestionWithAnswers(@PathVariable Long quizId, @RequestBody QuizQuestion question) {
        serviceQuiz.addQuestionWithAnswers(quizId, question, question.getQuizAnswers());
    }
    @PutMapping("/update_question")
    public QuizQuestion updateQuestion(@RequestBody QuizQuestion updatedQuestion) {
        return serviceQuiz.updateQuestion(updatedQuestion);
    }
    @PutMapping("/answers_update")
    public QuizAnswer updateAnswer(@RequestBody QuizAnswer updatedAnswer) {
        return serviceQuiz.updateAnswer(updatedAnswer);
    }
    @DeleteMapping("/DeleteQuestion/{idQ}")
    public void deleteQuestion(@PathVariable Long idQ) {
        serviceQuiz.deleteQuizQuestion(idQ);
    }

}
