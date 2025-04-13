package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Partnership;
import tn.esprit.entities.User;
@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, Long> {

}