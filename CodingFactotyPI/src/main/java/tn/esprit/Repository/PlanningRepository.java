package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.entities.FeedBackEvent;
import tn.esprit.entities.Planning;

import java.util.List;

public interface PlanningRepository extends JpaRepository<Planning, Long> {

    @Query("SELECT p FROM Planning p WHERE p.event.idEvent = :eventId")
    List<Planning> findByEventId(@Param("eventId") Long eventId);
}
