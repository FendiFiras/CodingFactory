package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Evaluation;
import tn.esprit.entities.User;
import tn.esprit.repositories.AssignmentRepository;
import tn.esprit.repositories.EvaluationRepository;
import tn.esprit.repositories.UserRepo;

import java.util.List;

@Service
public class EvaluationService implements IEvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Create an evaluation for a specific user
    @Override
    public Evaluation createEvaluation(Long assignmentId, Evaluation evaluation) {
        // Fetch the assignment by ID
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));

        // Ensure the assignment does not already have an evaluation (if needed)
        if (assignment.getEvaluation() != null) {
            throw new RuntimeException("This assignment already has an evaluation.");
        }

        // Link the evaluation to the assignment
        evaluation.setAssignment(assignment);

        // Save the evaluation
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        // Update the assignment with the new evaluation
        assignment.setEvaluation(savedEvaluation);
        assignmentRepository.save(assignment);

        return savedEvaluation;
    }

    // Get all evaluations
    @Override

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    // Get evaluation for a specific user
    @Override

    public Evaluation getEvaluationByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getEvaluation(); // Assuming @OneToOne relationship
    }

    // Update an evaluation
    @Override

    public Evaluation updateEvaluation(Long evaluationId, Evaluation updatedEvaluation) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));
        evaluation.setScore(updatedEvaluation.getScore());
        evaluation.setComment(updatedEvaluation.getComment());
        return evaluationRepository.save(evaluation);
    }

    // Delete an evaluation
    @Override

    public void deleteEvaluation(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        // Remove the evaluation from the user
        User user = userRepository.findByEvaluation(evaluation);
        if (user != null) {
            user.setEvaluation(null); // Remove the evaluation reference
            userRepository.save(user);
        }

        // Delete the evaluation
        evaluationRepository.delete(evaluation);
    }
}
