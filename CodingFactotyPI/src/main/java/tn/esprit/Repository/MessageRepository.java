package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
