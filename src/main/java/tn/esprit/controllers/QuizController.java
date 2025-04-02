package tn.esprit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Quiz;
import tn.esprit.entities.QuizAnswer;
import tn.esprit.entities.QuizQuestion;
import tn.esprit.services.GeminiService;
import tn.esprit.services.ServiceQuiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/Quizs")
public class QuizController {

    @Autowired
    private ServiceQuiz serviceQuiz;
    @Autowired
    private GeminiService geminiService;


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

    @GetMapping("/GetAllquestion")
    public List<QuizQuestion> getAllquest() {
        return serviceQuiz.getAllQuestion();
    }


    // ✅ Endpoint pour récupérer les questions et réponses d'un quiz par ID
    @GetMapping("/quiz/{quizId}/questions")
    public Set<QuizQuestion> getQuestionsByQuiz(@PathVariable Long quizId) {
        return serviceQuiz.getQuestionsByQuiz(quizId);
    }


    @GetMapping("/questions/{questionId}/answers")
    public Set<QuizAnswer> getAnswersByQuestion(@PathVariable Long questionId) {
        return serviceQuiz.getAnswersByQuestionId(questionId);
    }

    // ✅ Endpoint pour ajouter une réponse à une question existante
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<QuizQuestion> addAnswerToQuestion(
            @PathVariable Long questionId,
            @RequestBody QuizAnswer newAnswer) {

        QuizQuestion updatedQuestion = serviceQuiz.addAnswerToQuestion(questionId, newAnswer);
        return ResponseEntity.ok(updatedQuestion);
    }

    @GetMapping("/training/{trainingId}")
    public List<Quiz> getQuizzesByTraining(@PathVariable Long trainingId) {
        return serviceQuiz.getQuizzesByTraining(trainingId);
    }

    @PostMapping("/submit-and-score/{userId}/{quizId}")
    public ResponseEntity<Map<String, Object>> submitAndCalculateScore(
            @PathVariable Long userId,
            @PathVariable Long quizId,
            @RequestBody List<Long> selectedAnswers) {

        Map<String, Object> result = serviceQuiz.submitAndCalculateScore(userId, quizId, selectedAnswers);
        return ResponseEntity.ok(result);
    }













    @PostMapping("/generate")
    public String generateQuiz(@RequestParam String topic, @RequestParam int numberOfQuestions) {
        return geminiService.generateQuizQuestions(topic, numberOfQuestions);
    }


    @PostMapping("/generate-questions/{quizId}")
    public ResponseEntity<Quiz> generateQuestions(
            @PathVariable Long quizId,
            @RequestParam String topic,
            @RequestParam int numberOfQuestions) {

        Quiz updatedQuiz = serviceQuiz.generateQuestionsForExistingQuiz(quizId, topic, numberOfQuestions);
        return ResponseEntity.ok(updatedQuiz);
    }





}


/**

    // ✅ Endpoint pour récupérer le score et le statut de réussite du quiz
    @GetMapping("/calculate-score/{quizId}/{userId}")
    public ResponseEntity<Map<String, Object>> calculateQuizScore(
            @PathVariable Long quizId,
            @PathVariable Long userId) {

        // 🔍 Appel du service pour calculer le score
        Map<String, Object> result = serviceQuiz.calculateQuizScore(quizId, userId);

        // ✅ Retourner la réponse JSON
        return ResponseEntity.ok(result);
    }
            **/
