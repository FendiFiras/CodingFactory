package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.QuizAnswer;

@Repository
public interface QuizAnswerRepo extends JpaRepository<QuizAnswer, Long> {
}
