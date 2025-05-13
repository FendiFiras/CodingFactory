package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Discussion;
import tn.esprit.entities.Message;

import java.util.Optional;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    Discussion findByMessagesContaining(Message message);
    @Query("SELECT d FROM Discussion d LEFT JOIN FETCH d.messages WHERE d.discussion_id = :discussionId")
    Optional<Discussion> findByIdWithMessages(@Param("discussionId") Long discussionId);
}