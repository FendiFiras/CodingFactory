package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Application;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Offer;
import tn.esprit.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("SELECT a FROM Assignment a WHERE a.user.idUser = :userId")
    Optional<Assignment> findByUserId(@Param("userId") Long userId);

    List<Assignment> findByOffer(Offer offer); // Use Offer entity instead of offerId

}
