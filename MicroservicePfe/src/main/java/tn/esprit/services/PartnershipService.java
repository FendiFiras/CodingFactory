package tn.esprit.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Partnership;
import tn.esprit.entities.Role;
import tn.esprit.entities.User;
import tn.esprit.repositories.PartnershipRepository;
import tn.esprit.repositories.UserRepo;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnershipService implements IPartnershipService {
    private final PartnershipRepository partnershipRepository;

    private final UserRepo userRepository;

    @Override
    public Partnership applyForPartnership(Partnership partnership, Long userId) {
        // Fetch the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if the user's role is COMPANY_REPRESENTATIVE
        if (!user.getRole().equals(Role.COMPANYREPRESENTIVE)) {
            throw new RuntimeException("Only  COMPANY REPRESENTATIVE can apply for partnership");
        }

        // Check if the user already has an active partnership
        if (user.getPartnership() != null) {
            throw new RuntimeException("You already has an active partnership and cannot apply for another.");
        }

        // Ensure the user has a company name
        if (user.getCompanyName() == null || user.getCompanyName().isEmpty()) {
            throw new RuntimeException("User must have a company name to apply for partnership");
        }

        // Set the company name in the partnership from the user's company name
        partnership.setCompanyName(user.getCompanyName());

        // Save the partnership first
        Partnership savedPartnership;
        try {
            savedPartnership = partnershipRepository.save(partnership);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw new RuntimeException("Error saving partnership");
        }

        // Set the partnership in the user entity
        user.setPartnership(savedPartnership);

        // Update the user entity
        try {
            userRepository.save(user);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw new RuntimeException("Error updating user with partnership");
        }

        return savedPartnership;
    }


    @Override
    public User getUserDetails(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public List<Partnership> getAllPartnerships() {
        try {
            return partnershipRepository.findAll();
        } catch (Exception e) {
            // Log the exception using a proper logging framework
            throw new RuntimeException("Error fetching partnerships: " + e.getMessage());
        }
    }

    @Override
    public Optional<Partnership> getPartnershipById(Long id) {
        return partnershipRepository.findById(id);
    }

    @Override
    public Partnership updatePartnership(Long id, Partnership partnership) {
        partnership.setIdPartnership(id);
        return partnershipRepository.save(partnership);
    }

    @Override
    public void deletePartnership(Long id) {
        partnershipRepository.deleteById(id);
    }
}