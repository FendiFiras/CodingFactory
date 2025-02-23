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
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ServiceQuiz implements IServiceQuiz {


    QuizRepo  quizRepo;
    QuizQuestionRepo quizQuestionRepo;
    QuizAnswerRepo quizAnswerRepo;


    public List<QuizQuestion> getAllQuestion() {
        return quizQuestionRepo.findAll();
    }
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
    public QuizQuestion addQuestionWithAnswers(Long quizId, QuizQuestion question, Set<QuizAnswer> answers) {
        // Récupérer le quiz
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId));

        // Associer la question au quiz
        question.setQuizAnswers(answers);

        // Sauvegarder la question avec ses réponses
        quizQuestionRepo.save(question);

        // Ajouter la question au quiz et sauvegarder
        quiz.getQuizQuestions().add(question);
        quizRepo.save(quiz);

        return question;
    }
    public QuizQuestion updateQuestion(QuizQuestion updatedQuestion) {
        QuizQuestion existingQuestion = quizQuestionRepo.findById(updatedQuestion.getIdQuizQ())
                .orElseThrow(() -> new IllegalArgumentException("Question introuvable avec ID: " + updatedQuestion.getIdQuizQ()));

        existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
        existingQuestion.setMaxGrade(updatedQuestion.getMaxGrade());

        return quizQuestionRepo.save(existingQuestion);
    }

    public QuizAnswer updateAnswer(QuizAnswer updatedAnswer) {
        QuizAnswer existingAnswer = quizAnswerRepo.findById(updatedAnswer.getIdQuizA())
                .orElseThrow(() -> new IllegalArgumentException("Réponse introuvable avec ID: " + updatedAnswer.getIdQuizA()));

        existingAnswer.setAnswerText(updatedAnswer.getAnswerText());
        existingAnswer.setCorrect(updatedAnswer.isCorrect());

        return quizAnswerRepo.save(existingAnswer);
    }

    public void deleteQuizQuestion(Long quizQuestionId) {
        // Récupérer la question à supprimer
        QuizQuestion quizQuestion = quizQuestionRepo.findById(quizQuestionId)
                .orElseThrow(() -> new RuntimeException("QuizQuestion non trouvée"));

        // Récupérer tous les Quiz
        List<Quiz> quizzes = quizRepo.findAll();
        Quiz quizContainingQuestion = null;

        // Trouver le quiz qui contient la question
        for (Quiz quiz : quizzes) {
            if (quiz.getQuizQuestions().contains(quizQuestion)) {
                quizContainingQuestion = quiz;
                break;
            }
        }

        // Si la question est associée à un quiz, on la dissocie
        if (quizContainingQuestion != null) {
            quizContainingQuestion.getQuizQuestions().remove(quizQuestion);
            quizRepo.save(quizContainingQuestion); // Mettre à jour le Quiz
        }

        // Supprimer la question après dissociation
        quizQuestionRepo.delete(quizQuestion);
    }
    public Set<QuizQuestion> getQuestionsByQuiz(Long quizId) {
        Optional<Quiz> quiz = quizRepo.findById(quizId);
        if (quiz.isPresent()) {
            return quiz.get().getQuizQuestions(); // ✅ Récupération des questions du quiz
        } else {
            throw new RuntimeException("❌ Quiz non trouvé avec l'ID : " + quizId);
        }
    }
    public Set<QuizAnswer> getAnswersByQuestionId(Long questionId) {
        Optional<QuizQuestion> question = quizQuestionRepo.findById(questionId);

        if (question.isEmpty()) {
            throw new RuntimeException("❌ Question non trouvée avec l'ID : " + questionId);
        }

        return question.get().getQuizAnswers();
    }
    public QuizQuestion addAnswerToQuestion(Long questionId, QuizAnswer newAnswer) {
        QuizQuestion question = quizQuestionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("❌ Question introuvable avec ID : " + questionId));

        // ✅ Ajouter la réponse à la liste des réponses de la question
        question.getQuizAnswers().add(newAnswer);

        // ✅ Sauvegarder la question (et automatiquement les réponses car `CascadeType.ALL` est activé)
        return quizQuestionRepo.save(question);
    }


}
