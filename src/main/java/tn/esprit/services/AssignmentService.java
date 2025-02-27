package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
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


    //eate an assignment for a specific user
    @Override
    public Assignment createAssignment(Long userId, Long offerId, Assignment assignment) {
        // Fetch the user (student) by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Fetch the offer by offerId
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + offerId));

        // Find the application made by the student for the specific offer
        Application application = user.getApplications().stream()
                .filter(app -> app.getOffer().getIdOffer().equals(offerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Application not found for the specified user and offer"));

        // Update the status of the application to "Affected"
        application.setStatus("Affected");
        applicationRepository.save(application);

        // Set the user and offer in the assignment
        assignment.setUser(user);
        assignment.setOffer(offer);

        // Save the assignment
        Assignment savedAssignment = assignmentRepository.save(assignment);

        // Set the assignment in the user (if needed)
        user.setAssignment(savedAssignment);
        userRepository.save(user);

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
}