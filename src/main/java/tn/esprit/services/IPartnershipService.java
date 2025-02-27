package tn.esprit.services;

import org.springframework.http.ResponseEntity;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Partnership;
import tn.esprit.entities.User;

import java.util.List;
import java.util.Optional;

public interface IPartnershipService {

    public Partnership applyForPartnership(Partnership partnership, Long userId)  ;
    public User getUserDetails(Long userId);
    List<Partnership> getAllPartnerships();
    Optional<Partnership> getPartnershipById(Long id);
    Partnership updatePartnership(Long id, Partnership partnership);
    void deletePartnership(Long id);
}