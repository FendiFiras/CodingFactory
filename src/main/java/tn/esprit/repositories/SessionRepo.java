package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Session;

import java.util.List;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {


    // ✅ Requête JPQL optimisée
    @Query("SELECT s FROM Session s WHERE s.courses.training.trainingId = :trainingId")
    List<Session> findSessionsByTrainingId(@Param("trainingId") Long trainingId);
}
