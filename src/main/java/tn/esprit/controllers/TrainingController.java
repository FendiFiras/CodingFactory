package tn.esprit.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Training;
import tn.esprit.repositories.TrainingRepository;
import tn.esprit.services.IServiceTraining;

import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    @Autowired
    private IServiceTraining serviceTraining; // Injection automatique du service

    @PostMapping("/addtraining/{userId}")
    public Training addTraining(@RequestBody Training training, @RequestParam Long userId) {


       return  serviceTraining.addTraining(training, userId); // Enregistrement de la formation


    }


    @DeleteMapping("/delete/{trainingId}")
    public String deleteTraining(@PathVariable Long trainingId) {
        try {
            // Appeler la méthode du service pour supprimer le training
            serviceTraining.deleteTraining(trainingId);
            return "Training deleted successfully.";
        } catch (IllegalArgumentException e) {
            return "Training not found with ID: " + trainingId;
        }
    }
    @PutMapping("/updatetraining")
    public Training updateTraining(@RequestBody Training training) {
        return serviceTraining.updateTraining(training); // Retourne directement l'objet mis à jour
    }
    @GetMapping("/List_training/{idt}")
    public Training getTrainingById(@PathVariable Long idt) {
        return serviceTraining.getOneById(idt);
    }
    @GetMapping("/trainings")
    public List<Training> getAllTrainings() {
        return serviceTraining.getAllTrainings();
    }
    @PutMapping("/assign-quiz/{trainingId}/{quizId}")
    public void affecterQuizTraining(@PathVariable Long trainingId, @PathVariable Long quizId) {
        serviceTraining.affecterQuizTraining(trainingId, quizId);
    }


}
