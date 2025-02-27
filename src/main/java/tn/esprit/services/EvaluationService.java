package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Evaluation;
import tn.esprit.entities.User;
import tn.esprit.repositories.EvaluationRepository;
import tn.esprit.repositories.UserRepo;

import java.util.List;

@Service
public class EvaluationService implements IEvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private UserRepo userRepository;

    // Create an evaluation for a specific user
    @Override
    public Evaluation createEvaluation(Long userId, Evaluation evaluation) {
        // Fetch the user (student) by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if the user has an assignment
        if (user.getAssignment() == null) {
            throw new RuntimeException("User is not affected. Cannot create evaluation.");
        }

        // Save the evaluation first to generate its ID
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        // Set the evaluation in the user
        user.setEvaluation(savedEvaluation);

        // Save the user (which will update the foreign key reference)
        userRepository.save(user);

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
