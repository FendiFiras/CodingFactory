package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Message;
import tn.esprit.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN u.forums f WHERE f.forum_id = :forumId")
    List<User> findUsersByForumId(@Param("forumId") Long forumId);

    @Query("SELECT u FROM User u JOIN u.discussions d WHERE d.discussion_id = :discussionId")
    List<User> findUsersByDiscussionId(@Param("discussionId") Long discussionId);

    User findByMessagesContaining(Message message);
}
