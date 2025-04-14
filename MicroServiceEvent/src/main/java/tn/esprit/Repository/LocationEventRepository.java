package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.LocationEvent;

public interface LocationEventRepository extends JpaRepository<LocationEvent, Long> {


}
