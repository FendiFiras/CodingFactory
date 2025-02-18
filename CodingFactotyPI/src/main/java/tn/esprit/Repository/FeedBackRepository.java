package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.FeedBackEvent;

public interface FeedBackRepository extends JpaRepository<FeedBackEvent, Long> {


}
