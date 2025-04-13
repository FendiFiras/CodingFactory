package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.QuizQuestion;

@Repository
public interface QuizQuestionRepo extends JpaRepository<QuizQuestion, Long> {
}
