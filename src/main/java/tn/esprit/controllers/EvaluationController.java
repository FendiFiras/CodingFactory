package tn.esprit.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Evaluation;
import tn.esprit.services.IAssignmentService;
import tn.esprit.services.IEvaluationService;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular app

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;

    // Create an evaluation for a user
    @PostMapping("/create")
    public ResponseEntity<?> createEvaluation(
            @RequestParam Long userId,
            @RequestBody Evaluation evaluation) {
        try {
            // Call the service method to create the evaluation
            Evaluation createdEvaluation = evaluationService.createEvaluation(userId, evaluation);
            return new ResponseEntity<>(createdEvaluation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Handle exceptions and return an appropriate error response
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all evaluations
    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return new ResponseEntity<>(evaluations, HttpStatus.OK);
    }

    // Get evaluation for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<Evaluation> getEvaluationByUser(@PathVariable Long userId) {
        Evaluation evaluation = evaluationService.getEvaluationByUser(userId);
        return new ResponseEntity<>(evaluation, HttpStatus.OK);
    }

    // Update an evaluation
    @PutMapping("/updateevaluation/{evaluationId}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long evaluationId, @RequestBody Evaluation updatedEvaluation) {
        Evaluation evaluation = evaluationService.updateEvaluation(evaluationId, updatedEvaluation);
        return new ResponseEntity<>(evaluation, HttpStatus.OK);
    }

    // Delete an evaluation
    @DeleteMapping("/updateevaluation/{evaluationId}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long evaluationId) {
        evaluationService.deleteEvaluation(evaluationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}