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

    @Query(value = "SELECT u.first_name, u.last_name " +
            "FROM user_applications ua " +
            "JOIN user u ON ua.user_id_user = u.id_user " +
            "WHERE ua.applications_id_application = :applicationId", nativeQuery = true)
    List<Object[]> findUserNameByApplicationId(@Param("applicationId") Long applicationId);

    @Query("SELECT a FROM Application a WHERE a.offer.idOffer = :offerId")
    List<Application> findByOfferId(@Param("offerId") Long offerId);


}
