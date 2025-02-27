package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Evaluation;
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
