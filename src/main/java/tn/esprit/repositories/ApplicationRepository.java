package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Application;
import tn.esprit.entities.Offer;
import tn.esprit.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByOfferIn(List<Offer> offers);
    List<Application> findByOffer(Offer offer);

    @Query("SELECT u.idUser FROM User u JOIN u.applications a WHERE a.idApplication = :applicationId")
    Long findUserIdByApplicationId(@Param("applicationId") Long applicationId);
}
