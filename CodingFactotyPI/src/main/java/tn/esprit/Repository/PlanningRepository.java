package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Planning;

public interface PlanningRepository extends JpaRepository<Planning, Long> {


}
