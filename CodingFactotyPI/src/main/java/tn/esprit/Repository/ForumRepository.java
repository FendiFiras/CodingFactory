package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Forum;

public interface ForumRepository extends JpaRepository<Forum, Long> {
}
