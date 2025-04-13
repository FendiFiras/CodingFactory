package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Evaluation;
import tn.esprit.entities.Interview;

import java.util.List;

@Repository
public interface interveiwRepo extends JpaRepository<Interview, Long> {
    @Query("SELECT i FROM Interview i WHERE i.application.idApplication IN (" +
            "SELECT a.idApplication FROM User u JOIN u.applications a WHERE u.idUser = :userId)")
    List<Interview> findInterviewsByStudentId(@Param("userId") Long userId);
    @Query("SELECT COUNT(i) > 0 FROM Interview i WHERE i.application.idApplication = :applicationId")
    boolean existsByApplicationId(@Param("applicationId") Long applicationId);
}
