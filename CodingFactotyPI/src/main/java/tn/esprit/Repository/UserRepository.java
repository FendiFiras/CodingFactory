package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.entities.User;

import java.util.List;  // Correction ici

public interface UserRepository extends JpaRepository<User, Long> {

    // Requête personnalisée pour récupérer les utilisateurs associés à un forum
    @Query("SELECT u FROM User u JOIN u.forums f WHERE f.forum_id = :forumId")
    List<User> findUsersByForumId(@Param("forumId") Long forumId);

    @Query("SELECT u FROM User u JOIN u.discussions d WHERE d.discussion_id = :discussionId")
    List<User> findUsersByDiscussionId(Long discussionId);

}
