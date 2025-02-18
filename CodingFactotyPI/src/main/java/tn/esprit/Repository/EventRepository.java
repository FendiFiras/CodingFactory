package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Event;

public interface EventRepository extends JpaRepository<Event, Long> {


}
