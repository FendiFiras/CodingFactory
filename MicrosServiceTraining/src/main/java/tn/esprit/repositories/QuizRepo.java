package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Quiz;
import tn.esprit.entities.User;

import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {


    @Query("SELECT q FROM Quiz q WHERE q.training.trainingId = :trainingId")
    List<Quiz> findQuizzesByTraining(@Param("trainingId") Long trainingId);
    // Requête JPQL pour récupérer les utilisateurs qui ont passé un quiz spécifique
    @Query("SELECT u FROM User u JOIN u.quizss q WHERE q.idQuiz = :idQuiz")
    List<User> findUsersByQuizId(@Param("idQuiz") Long idQuiz);
}
