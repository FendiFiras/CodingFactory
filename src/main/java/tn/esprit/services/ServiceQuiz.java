package tn.esprit.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Quiz;
import tn.esprit.entities.QuizAnswer;
import tn.esprit.entities.QuizQuestion;
import tn.esprit.repositories.QuizAnswerRepo;
import tn.esprit.repositories.QuizQuestionRepo;
import tn.esprit.repositories.QuizRepo;

import java.util.List;
@Service
@AllArgsConstructor
public class ServiceQuiz implements IServiceQuiz {


    QuizRepo  quizRepo;
    QuizQuestionRepo quizQuestionRepo;
    QuizAnswerRepo quizAnswerRepo;

    public Quiz addQuiz(Quiz quiz) {
        return quizRepo.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepo.findAll();
    }

    public Quiz getQuizById(Long quizId) {
        return quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId));
    }

    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepo.findById(quizId).orElse(null);
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz not found with ID: " + quizId);
        }
        quizRepo.delete(quiz);
    }

    public Quiz updateQuiz(Quiz quiz) {
        Quiz existingQuiz = quizRepo.findById(quiz.getIdQuiz())
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quiz.getIdQuiz()));

        // Mettre à jour les propriétés de l'objet existant avec celles du nouvel objet
        existingQuiz.setQuizName(quiz.getQuizName());
        existingQuiz.setDeadline(quiz.getDeadline());
        existingQuiz.setMinimumGrade(quiz.getMinimumGrade());
        existingQuiz.setTimeLimit(quiz.getTimeLimit());
        existingQuiz.setMaxGrade(quiz.getMaxGrade());

        // Sauvegarder les changements dans la base de données
        return quizRepo.save(existingQuiz);
    }

}
