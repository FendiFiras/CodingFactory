package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.entities.FeedBackEvent;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBackEvent, Long> {

    @Query("SELECT f FROM FeedBackEvent f WHERE f.event.idEvent = :eventId")
    List<FeedBackEvent> findByEventId(@Param("eventId") Long eventId);
}
