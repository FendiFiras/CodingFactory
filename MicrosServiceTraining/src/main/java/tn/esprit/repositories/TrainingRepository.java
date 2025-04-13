package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Training;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t JOIN t.Users u WHERE u.idUser = :userId")
    List<Training> findTrainingsByUserId(@Param("userId") Long userId);
    @Query("SELECT t FROM Training t JOIN t.Quiz q WHERE q.idQuiz = :quizId")
    List<Training> findTrainingsByQuizId(@Param("quizId") Long quizId);
    @Query("SELECT t FROM Training t JOIN t.coursess c WHERE c.courseId = :courseId")
    List<Training> findTrainingsByCourseId(@Param("courseId") Long courseId);




    @Query("SELECT t FROM Training t WHERE t NOT IN (SELECT u.trainings FROM User u WHERE u.idUser= :userId)")
    List<Training> findTrainingsNotEnrolledByUser(@Param("userId") Long userId);

    List<Training> findTop5ByOrderByStartDateDesc();
    @Query("SELECT COUNT(t) FROM Training t JOIN t.Users u WHERE u.idUser = :userId")
    long countTrainingsByUserId(@Param("userId") Long userId);


}



