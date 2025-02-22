package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Discussion;
import tn.esprit.entities.Message;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    Discussion findByMessagesContaining(Message message);
}

