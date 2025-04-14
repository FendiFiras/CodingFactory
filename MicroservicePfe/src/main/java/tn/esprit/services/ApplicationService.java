package tn.esprit.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.entities.*;
import tn.esprit.repositories.ApplicationRepository;
import tn.esprit.repositories.OfferRepository;
import tn.esprit.repositories.UserRepo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService implements IApplicationService {
    private final ApplicationRepository applicationRepository;
    private final OfferRepository offerRepository;
    private final UserRepo userRepository;
    private final AiScoreService aiScoreService;





    public List<Application> getApplicationsWithScores(Long offerId, String requiredSkills) {
        // Fetch all applications for the given offer ID
        List<Application> applications = applicationRepository.findByOfferId(offerId);

        // Loop through each application and calculate the AI score
        for (Application app : applications) {
            // Check if the CV field is not null or empty
            String cvFilename = app.getCV();
            if (cvFilename != null && !cvFilename.isEmpty()) {
                // Process the CV file and calculate the score
                File file = Paths.get("uploads", "cvs", cvFilename).toFile();
                try {
                    float score = aiScoreService.calculateMatchScore(file, requiredSkills);
                    app.setScore(score);  // Assign the calculated score to the application
                } catch (IOException e) {
                    // Log the exception and set score to 0 in case of error
                    System.err.println("Error processing CV file for application ID " + app.getIdApplication() + ": " + e.getMessage());
                    app.setScore(0f);  // In case of error, set score to 0
                } catch (NumberFormatException e) {
                    // Log and handle unexpected format issues (e.g., invalid float)
                    System.err.println("Error parsing score for application ID " + app.getIdApplication() + ": " + e.getMessage());
                    app.setScore(0f);  // In case of error, set score to 0
                }
            } else {
                // Handle case where CV is missing or empty
                System.err.println("No CV provided for application ID " + app.getIdApplication());
                app.setScore(0f);  // In case of missing CV, set score to 0
            }
        }

        // Return the applications with their calculated scores
        return applications;
    }
    @Override
    public Application applyForOffer(Application application, Long userId) {
        // Fetch the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if the user's role is STUDENT
        if (!user.getRole().equals(Role.STUDENT)) {
            throw new RuntimeException("Only users with the role STUDENT can apply for offers");
        }

        // Fetch the Offer from the database
        Long offerId = application.getOffer().getIdOffer();
        Optional<Offer> offerOptional = offerRepository.findById(offerId);

        if (offerOptional.isPresent()) {
            Offer offer = offerOptional.get();

            // Set the submission date to the current date
            application.setSubmissionDate(new Date());

            // Associate the application with the offer
            application.setOffer(offer);

            // Add the application to the user's applications set
            user.getApplications().add(application);

            // Save the user (which will cascade the save operation to the application if cascading is enabled)
            userRepository.save(user);

            return application;
        } else {
            throw new RuntimeException("Offer not found with id: " + offerId);
        }
    }

@Override
public Long getUserIdByApplicationId(Long applicationId) {
    Long userId = applicationRepository.findUserIdByApplicationId(applicationId);
    if (userId != null) {
        return userId;
    } else {
        throw new RuntimeException("Application not found or no associated user.");
    }
}

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }
    @Override
    public Set<Application> getApplicationsByStudent(Long userId) {
        // Fetch the user by userId
        return userRepository.findApplicationsByUserId(userId);
    }
    @Override
    public List<Application> getApplicationsForCompanyRepresentative(Long userId) {
        // Fetch the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if the user's role is COMPANY_REPRESENTATIVE
        if (!user.getRole().equals(Role.COMPANYREPRESENTIVE)) {
            throw new RuntimeException("Only users with the role COMPANY_REPRESENTATIVE can access applications");
        }

        // Fetch the partnership associated with the user
        Partnership partnership = user.getPartnership();
        if (partnership == null) {
            throw new RuntimeException("User is not associated with any partnership");
        }

        // Fetch all offers associated with the partnership
        List<Offer> offers = offerRepository.findByPartnership(partnership);

        // Fetch all applications for the offers
        return applicationRepository.findByOfferIn(offers);
    }

    @Override
    public List<Application> getApplicationsByOfferId(Long offerId) {
        // Fetch the offer by offerId
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + offerId));

        // Fetch all applications associated with the offer
        return applicationRepository.findByOffer(offer);
    }
    @Override
    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    @Override
    public Application updateApplication(Long id, Application application) {
        Application existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        // Update the fields
        existingApplication.setFieldofStudy(application.getFieldofStudy());
        existingApplication.setUniversity(application.getUniversity());
        existingApplication.setStatus(application.getStatus());
        existingApplication.setScore(application.getScore());
        existingApplication.setSubmissionDate(application.getSubmissionDate());
        existingApplication.setAvailability(application.getAvailability());
        existingApplication.setCoverLetter(application.getCoverLetter());
        existingApplication.setCV(application.getCV());

        return applicationRepository.save(existingApplication);
    }

    @Override
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public String getApplicantFullName(Long applicationId) {
        List<Object[]> result = applicationRepository.findUserNameByApplicationId(applicationId);
        if (!result.isEmpty()) {
            Object[] row = result.get(0);
            return row[0] + " " + row[1]; // firstName + lastName
        }
        return null;
    }
}
