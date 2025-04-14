package tn.esprit.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Evaluation;
import tn.esprit.entities.User;
import tn.esprit.repositories.AssignmentRepository;
import tn.esprit.repositories.EvaluationRepository;
import tn.esprit.repositories.UserRepo;
import tn.esprit.services.EmailService;
import tn.esprit.services.IAssignmentService;
import tn.esprit.services.IEvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular app
@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;
    @Autowired

    private EvaluationRepository evaluationRepository;

    @Autowired

    private AssignmentRepository assignmentRepository;
    private static final Logger log = LoggerFactory.getLogger(EvaluationController.class);

    // Create an evaluation for a user
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvaluation(
            @RequestParam Long assignmentId,
            @RequestPart("evaluation") Evaluation evaluation,
            @RequestPart("pdfFile") MultipartFile pdfFile) {
        try {
            // Convert PDF file to byte array
            evaluation.setEvaluationPdf(pdfFile.getBytes());

            // Save evaluation
            Evaluation createdEvaluation = evaluationService.createEvaluation(assignmentId, evaluation);
            return new ResponseEntity<>(createdEvaluation, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{evaluationId}/pdf")
    public ResponseEntity<byte[]> getEvaluationPdf(@PathVariable Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"evaluation_" + evaluationId + ".pdf\"") // Dynamic filename
                .contentType(MediaType.APPLICATION_PDF)
                .body(evaluation.getEvaluationPdf());
    }

    @GetMapping("/assignments/offer/{offerId}")
    public List<Assignment> getAssignmentsByOfferId(@PathVariable Long offerId) {
        if (offerId == null) {
            // Handle null value, maybe return empty list or an error message
            return Collections.emptyList();
        }
        return assignmentRepository.findByOfferIdWithEvaluation(offerId);
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