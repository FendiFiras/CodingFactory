package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Response;

import java.util.List;

@Repository
public interface ResponseREpo extends JpaRepository<Response, Long> {
    @Query("SELECT r FROM Response r " +
            "WHERE r.quiz.idQuiz = :quizId " +
            "AND r.quiz.training.trainingId IN " +
            "(SELECT t.trainingId FROM Training t JOIN t.Users u WHERE u.idUser = :userId)")
    List<Response> findResponsesByQuizAndUser(@Param("quizId") Long quizId, @Param("userId") Long userId);



}
