package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.entities.Event;
import tn.esprit.entities.Registration;
import tn.esprit.entities.User;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event.idEvent = :idEvent")
    long countConfirmedParticipantsByEventId(@Param("idEvent") Long idEvent);

    long countByEvent(Event event);
    boolean existsByEventAndUser(Event event, User user);
}
