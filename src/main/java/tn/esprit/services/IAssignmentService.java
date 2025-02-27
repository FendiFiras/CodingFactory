package tn.esprit.services;

import tn.esprit.entities.Assignment;
import tn.esprit.entities.Offer;

import java.util.List;

public interface IAssignmentService {

    // Create an assignment for a specific user
    public Assignment createAssignment(Long userId, Long offerId, Assignment assignment);
    public Assignment getAssignmentByUserId(Long userId);
    public List<Assignment> getAssignmentsByOffer(Offer offer);

    // Get all assignments
    List<Assignment> getAllAssignments();

    // Get assignment for a specific user
    Assignment getAssignmentByUser(Long userId);

    // Update an assignment
    Assignment updateAssignment(Long assignmentId, Assignment updatedAssignment);

    // Delete an assignment
    void deleteAssignment(Long assignmentId);
}
