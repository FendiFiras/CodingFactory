package tn.esprit.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import tn.esprit.entities.*;
import tn.esprit.repositories.ApplicationRepository;
import tn.esprit.repositories.AssignmentRepository;
import tn.esprit.repositories.OfferRepository;
import tn.esprit.repositories.UserRepo;

import java.util.List;

@Service
public class AssignmentService implements IAssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private  OfferRepository offerRepository;
    @Autowired
    private  ApplicationRepository applicationRepository;
    @Autowired
    private  EmailService emailService;

    //eate an assignment for a specific user
    @Override
    public Assignment createAssignment(Long userId, Long offerId, Assignment assignment) {
        // Existing code remains unchanged
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + offerId));

        Application application = user.getApplications().stream()
                .filter(app -> app.getOffer().getIdOffer().equals(offerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus("Affected");
        applicationRepository.save(application);

        assignment.setUser(user);
        assignment.setOffer(offer);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        user.setAssignment(savedAssignment);
        userRepository.save(user);

        // ********** MAILING LOGIC START **********
        try {
            if(savedAssignment.getUser() != null && savedAssignment.getOffer() != null) {
                emailService.sendAssignmentNotification(
                        savedAssignment.getUser(),
                        savedAssignment.getOffer(),
                        savedAssignment
                );
                // Optional: Add logging for success
            }
        } catch (Exception e) {
            // Handle email exception without breaking the flow

        }
        // ********** MAILING LOGIC END **********

        return savedAssignment;
    }
    @Override
    public Assignment getAssignmentByUserId(Long userId) {
        return assignmentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Assignment not found for user with id: " + userId));
    }
    // Get all assignments
    @Override
    public List<Assignment> getAssignmentsByOffer(Offer offer) {
        return assignmentRepository.findByOffer(offer);
    }
    @Override
    public List<Assignment> getAssignmentsByOffer(Long offerId) {
        return assignmentRepository.findAssignmentsByOfferId(offerId);
    }
    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // Get assignment for a specific user
    @Override

    public Assignment getAssignmentByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getAssignment(); // Assuming @OneToOne relationship
    }

    // Update an assignment
    @Override

    public Assignment updateAssignment(Long assignmentId, Assignment updatedAssignment) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setStatus(updatedAssignment.getStatus());
        assignment.setStartDate(updatedAssignment.getStartDate());
        assignment.setEndDate(updatedAssignment.getEndDate());
        return assignmentRepository.save(assignment);
    }

    // Delete an assignment
    @Override

    public void deleteAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Remove the assignment from the user
        User user = userRepository.findByAssignment(assignment);
        if (user != null) {
            user.setAssignment(null); // Remove the assignment reference
            userRepository.save(user);
        }

        // Delete the assignment
        assignmentRepository.delete(assignment);
    }
    public String getUserFullName(Long assignmentId) {
        return assignmentRepository.findUserFullNameByAssignmentId(assignmentId);
    }

    public String getOfferTitle(Long assignmentId) {
        return assignmentRepository.findOfferTitleByAssignmentId(assignmentId);
    }
}