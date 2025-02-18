package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


}
