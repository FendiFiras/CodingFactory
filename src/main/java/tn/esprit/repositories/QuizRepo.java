package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Quiz;

import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {


    @Query("SELECT q FROM Quiz q WHERE q.training.trainingId = :trainingId")
    List<Quiz> findQuizzesByTraining(@Param("trainingId") Long trainingId);
}
