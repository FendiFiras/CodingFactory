package tn.esprit.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.entities.*;
import tn.esprit.repositories.*;

import java.util.*;

@Service
@AllArgsConstructor
public class ServiceQuiz implements IServiceQuiz {


    QuizRepo quizRepo;
    QuizQuestionRepo quizQuestionRepo;
    QuizAnswerRepo quizAnswerRepo;
    ResponseREpo responserepo;
    UserRepo userRepo;
    TrainingRepository trainingRepo;


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

        // 🔥 Vérifier si le quiz est lié à une formation
        if (quiz.getTraining() != null) {
            Training training = quiz.getTraining();
            training.setQuiz(null); // ✅ Supprimer l'association avec Training
            trainingRepo.save(training); // ✅ Sauvegarder la mise à jour du Training
        }

        // ✅ Supprimer le Quiz après avoir dissocié Training
        quizRepo.deleteById(quizId);
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

    public List<Quiz> getQuizzesByTraining(Long trainingId) {
        return quizRepo.findQuizzesByTraining(trainingId);
    }
//Quiz
public Map<String, Object> submitAndCalculateScore(Long userId, Long quizId, List<Long> selectedAnswers) {
    User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé !"));

    Quiz quiz = quizRepo.findById(quizId)
            .orElseThrow(() -> new RuntimeException("❌ Quiz non trouvé !"));

    int totalScore = 0;
    Set<Response> userResponses = new HashSet<>(); // 🔥 Stocker les réponses pour User

    for (Long answerId : selectedAnswers) {
        QuizAnswer answer = quizAnswerRepo.findById(answerId)
                .orElseThrow(() -> new RuntimeException("❌ Réponse ID " + answerId + " non trouvée !"));

        boolean isCorrect = answer.isCorrect();

        QuizQuestion question = quiz.getQuizQuestions().stream()
                .filter(q -> q.getQuizAnswers().contains(answer))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("❌ Question non trouvée pour réponse ID " + answerId));

        // ✅ Création de la réponse et liaison avec Quiz
        Response response = new Response();
        response.setGivenResponse(answer.getAnswerText());
        response.setCorrect(isCorrect);
        response.setQuiz(quiz);

        if (isCorrect) {
            response.setScoreObtained(true);
            totalScore += question.getMaxGrade(); // ✅ Ajoute la note de la question au total
        }

        userResponses.add(response); // ✅ Ajouter la réponse à l'utilisateur
    }

    // ✅ Affecter les réponses à User et sauvegarder
    user.setStudentResponses(userResponses);
    userRepo.save(user); // ✅ Sauvegarde de User avec ses réponses

    // ✅ Affecter Quiz à User et sauvegarder
    user.getQuizs().add(quiz);
    userRepo.save(user); // ✅ Mise à jour pour empêcher de repasser le quiz

    int maxScore = quiz.getMaxGrade();
    boolean passed = totalScore >= quiz.getMinimumGrade();

    // ✅ Retourner immédiatement le score final
    Map<String, Object> result = new HashMap<>();
    result.put("score", totalScore);
    result.put("maxScore", maxScore);
    result.put("passed", passed);
    result.put("message", passed ? "🎉 Félicitations, vous avez réussi !" : "❌ Échec, essayez encore.");

    System.out.println("✅ Score final de l'utilisateur : " + totalScore);
    return result;
}

/**

    public Map<String, Object> calculateQuizScore(Long quizId, Long userId) {
        // 🔍 Récupérer les réponses avec JPQL
        List<Response> responses = responserepo.findResponsesByQuizAndUser(quizId, userId);

        // 🎯 Calculer le score total (somme des maxGrade des réponses correctes)
        int totalScore = responses.stream()
                .filter(Response::isCorrect)
                .mapToInt(response -> {
                    QuizQuestion question = quizRepo.findById(quizId)
                            .orElseThrow(() -> new RuntimeException("Quiz not found"))
                            .getQuizQuestions().stream()
                            .filter(q -> q.getQuizAnswers().stream()
                                    .anyMatch(a -> a.getAnswerText().equals(response.getGivenResponse())))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Question not found"));
                    return question.getMaxGrade(); // ✅ On récupère la note de la question
                }).sum();

        // 📌 Récupérer le quiz et la note minimale
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        int maxScore = quiz.getMaxGrade(); // ✅ Score maximal possible du quiz
        boolean passed = totalScore >= quiz.getMinimumGrade(); // ✅ Succès si score >= note minimale

        // 📝 Construire la réponse JSON
        Map<String, Object> result = new HashMap<>();
        result.put("score", totalScore);
        result.put("maxScore", maxScore);
        result.put("passed", passed);
        result.put("message", passed ? "🎉 Félicitations, vous avez réussi !" : "❌ Échec, essayez encore.");

        return result;
    }
 **/
}







