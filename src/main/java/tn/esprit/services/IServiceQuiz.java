package tn.esprit.services;

import tn.esprit.entities.Quiz;
import tn.esprit.entities.QuizAnswer;
import tn.esprit.entities.QuizQuestion;

import java.util.List;

public interface IServiceQuiz {
    public Quiz addQuiz(Quiz quiz);
    public List<Quiz> getAllQuizzes();
    public Quiz getQuizById(Long quizId);
    public void deleteQuiz(Long quizId);
    Quiz updateQuiz(Quiz quiz);

}
