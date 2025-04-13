package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Offer;
import tn.esprit.entities.Partnership;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
            List<Offer> findByPartnership(Partnership partnership);


}