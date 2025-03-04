package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
