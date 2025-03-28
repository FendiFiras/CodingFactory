package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.entities.Event;
import tn.esprit.entities.EventType;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE (:s ='' OR " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :s, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :s, '%')) OR " +
            "LOWER(e.location) LIKE LOWER(CONCAT('%', :s, '%')))")
    List<Event> searchEvents(@Param("s") String s);

}





