package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.entities.Forum;

import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("SELECT f FROM Forum f JOIN f.discussions d WHERE d.discussion_id = :discussionId")
    List<Forum> findForumsByDiscussionId(@Param("discussionId") Long discussionId);

}
