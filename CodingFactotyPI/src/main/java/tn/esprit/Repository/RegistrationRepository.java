package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Event;
import tn.esprit.entities.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {


}
