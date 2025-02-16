package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Discussion;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}
