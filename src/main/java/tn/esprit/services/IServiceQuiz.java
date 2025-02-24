package tn.esprit.services;

import tn.esprit.entities.Quiz;
import tn.esprit.entities.QuizAnswer;
import tn.esprit.entities.QuizQuestion;
import tn.esprit.entities.Training;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IServiceQuiz {
    public Quiz addQuiz(Quiz quiz);
    public List<Quiz> getAllQuizzes();
    public Quiz getQuizById(Long quizId);
    public void deleteQuiz(Long quizId);
    Quiz updateQuiz(Quiz quiz);
    QuizQuestion addQuestionWithAnswers(Long quizId, QuizQuestion question, Set<QuizAnswer> answers);
    QuizQuestion updateQuestion(QuizQuestion updatedQuestion);
    QuizAnswer updateAnswer(QuizAnswer updatedAnswer);
    public List<QuizQuestion> getAllQuestion()     ;
    void deleteQuizQuestion(Long quizQuestionId);
    Set<QuizQuestion> getQuestionsByQuiz(Long quizId);
    List<Quiz> getQuizzesByTraining(Long trainingId);
   // void submitQuizResponses(Long userId, Long quizId, List<Long> selectedAnswers);
    Map<String, Object> submitAndCalculateScore(Long userId, Long quizId, List<Long> selectedAnswers);    //boolean calculateQuizScore(Long quizId, Long userId);
}
