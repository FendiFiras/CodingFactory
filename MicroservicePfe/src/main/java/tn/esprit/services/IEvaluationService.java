package tn.esprit.services;

import tn.esprit.entities.Evaluation;

import java.util.List;

public interface IEvaluationService {

    // Create an evaluation for a specific user
    Evaluation createEvaluation(Long userId, Evaluation evaluation);

    // Get all evaluations
    List<Evaluation> getAllEvaluations();

    // Get evaluation for a specific user
    Evaluation getEvaluationByUser(Long userId);

    // Update an evaluation
    Evaluation updateEvaluation(Long evaluationId, Evaluation updatedEvaluation);

    // Delete an evaluation
    void deleteEvaluation(Long evaluationId);
}