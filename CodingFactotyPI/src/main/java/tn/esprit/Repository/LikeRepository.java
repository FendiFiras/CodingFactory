package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}