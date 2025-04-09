package tn.esprit.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.*;
import tn.esprit.repositories.ApplicationRepository;
import tn.esprit.repositories.OfferRepository;
import tn.esprit.repositories.PartnershipRepository;
import tn.esprit.repositories.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService implements IOfferService {
    private final OfferRepository offerRepository;
    private final PartnershipRepository partnershipRepository;
    private final UserRepo userRepository;

    @Override
    public Offer createOffer(Offer offer, Long userId) {
        // Fetch the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if the user's role is COMPANY_REPRESENTATIVE
        if (!user.getRole().equals(Role.COMPANYREPRESENTIVE)) {
            throw new RuntimeException("Only users with the role COMPANY_REPRESENTATIVE can create offers");
        }

        // Check if the user has a partnership
        if (user.getPartnership() == null) {
            throw new RuntimeException("User does not have a partnership. Please apply for a partnership first.");
        }

        // Set the partnership in the offer from the user's partnership
        offer.setPartnership(user.getPartnership());

        // Ensure that required skills are properly handled as a comma-separated string
        if (offer.getRequiredSkill() != null && !offer.getRequiredSkill().isEmpty()) {
            // Convert the requiredSkills list (if any) into a comma-separated string
            offer.setRequiredSkill(String.join(",", offer.getRequiredSkill()));
        } else {
            offer.setRequiredSkill("");  // Ensure the field is not null if empty
        }

        // Save the offer
        return offerRepository.save(offer);
    }



    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    @Override
    public List<Offer> getOffersByCompanyRepresentative(Long userId) {
        // Fetch the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if the user's role is COMPANY_REPRESENTATIVE
        if (!user.getRole().equals(Role.COMPANYREPRESENTIVE)) {
            throw new RuntimeException("Only users with the role COMPANY_REPRESENTATIVE can access offers");
        }

        // Get the partnership ID from the user entity
        Partnership partnership = user.getPartnership();
        if (partnership == null) {
            throw new RuntimeException("User is not associated with any partnership");
        }
        Long partnershipId = partnership.getIdPartnership();

        // Fetch all offers associated with the same partnership ID
        return offerRepository.findByPartnership(partnership);
    }

    @Override
    public Offer updateOffer(Long id, Offer updatedOffer) {
        return offerRepository.findById(id).map(offer -> {
            // Update all fields from updatedOffer to the existing offer
            offer.setTitle(updatedOffer.getTitle());
            offer.setDescription(updatedOffer.getDescription());
            offer.setRequiredSkill(updatedOffer.getRequiredSkill());
            offer.setLocation(updatedOffer.getLocation());
            offer.setDuration(updatedOffer.getDuration());

            offer.setEmploymentType(updatedOffer.getEmploymentType());
            offer.setJobResponsibilities(updatedOffer.getJobResponsibilities());
            offer.setWhatWeOffer(updatedOffer.getWhatWeOffer());

            // Save the updated offer
            return offerRepository.save(offer);
        }).orElseThrow(() -> new RuntimeException("Offer not found with id: " + id));
    }

    @Override
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}