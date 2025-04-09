package tn.esprit.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Offer;
import tn.esprit.repositories.AssignmentRepository;
import tn.esprit.services.IAssignmentService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/assignments")
@CrossOrigin(origins = "*")  // Allow frontend requests

@RequiredArgsConstructor

public class AssignmentController {

    @Autowired

    private AssignmentRepository assignmentRepository;
    private final IAssignmentService assignmentService;

    // Create an assignment for a user
    @PostMapping("/create")
    public ResponseEntity<?> createAssignment(
            @RequestParam Long userId,
            @RequestParam Long offerId,
            @RequestBody Assignment assignment) {
        try {
            // Call the service method to create the assignment
            Assignment createdAssignment = assignmentService.createAssignment(userId, offerId, assignment);
            return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Handle exceptions and return an appropriate error response
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        return assignmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/by-offer/{offerId}")
    public List<Assignment> getAssignmentsByOffer(@PathVariable Long offerId) {
        if (offerId == null) {
            // Handle null value, maybe return empty list or an error message
            return Collections.emptyList();
        }
        return assignmentRepository.findByOfferIdWithEvaluation(offerId);
    }
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getAssignmentByUserId(@PathVariable Long userId) {
        try {
            // Call the service method to get the assignment by userId
            Assignment assignment = assignmentService.getAssignmentByUserId(userId);
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle exceptions and return an appropriate error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all assignments
    @GetMapping
    public ResponseEntity<List<Assignment>> getAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    // Get assignment for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<Assignment> getAssignmentByUser(@PathVariable Long userId) {
        Assignment assignment = assignmentService.getAssignmentByUser(userId);
        return new ResponseEntity<>(assignment, HttpStatus.OK);
    }

    // Update an assignment
    @PutMapping("/update-affectation/{assignmentId}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long assignmentId, @RequestBody Assignment updatedAssignment) {
        Assignment assignment = assignmentService.updateAssignment(assignmentId, updatedAssignment);
        return new ResponseEntity<>(assignment, HttpStatus.OK);
    }

    // Delete an assignment
    @DeleteMapping("/detete-affectation/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/{id}/user-name")
    public ResponseEntity<String> getUserFullName(@PathVariable("id") Long assignmentId) {
        String name = assignmentService.getUserFullName(assignmentId);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/{id}/offer-title")
    public ResponseEntity<String> getOfferTitle(@PathVariable("id") Long assignmentId) {
        String title = assignmentService.getOfferTitle(assignmentId);
        return ResponseEntity.ok(title);
    }
}
