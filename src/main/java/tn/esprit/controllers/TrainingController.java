package tn.esprit.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Training;
import tn.esprit.repositories.TrainingRepository;
import tn.esprit.services.IServiceTraining;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    @Autowired
    private IServiceTraining serviceTraining; // Injection automatique du service

    @PostMapping("/addtraining/{userId}")
    public Training addTraining(@RequestBody Training training, @PathVariable Long userId) {


       return  serviceTraining.addTraining(training, userId); // Enregistrement de la formation


    }


    @DeleteMapping("/delete/{trainingId}")
    public void deleteTraining(@PathVariable Long trainingId) {

            // Appeler la méthode du service pour supprimer le training
            serviceTraining.deleteTraining(trainingId);

        }




    @PutMapping("/updatetraining")
    public Training updateTraining(@RequestBody Training training) {
        return serviceTraining.updateTraining(training); // Retourne directement l'objet mis à jour
    }
    @GetMapping("/List_training/{idt}")
    public Training getTrainingById(@PathVariable Long idt) {
        return serviceTraining.getOneById(idt);
    }
    @GetMapping("/Get_alltrainings")
    public List<Training> getAllTrainings() {
        return serviceTraining.getAllTrainings();
    }
    @PutMapping("/assign-quiz/{trainingId}/{quizId}")
    public void affecterQuizTraining(@PathVariable Long trainingId, @PathVariable Long quizId) {
        serviceTraining.affecterQuizTraining(trainingId, quizId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Training>> getTrainingsByUser(@PathVariable Long userId) {
        List<Training> trainings = serviceTraining.getTrainingsByUser(userId);
        return ResponseEntity.ok(trainings);
    }


    // ✅ Endpoint pour récupérer les formations associées à un quiz
    @GetMapping("/quiz/{quizId}")
    public List<Training> getTrainingsByQuiz(@PathVariable Long quizId) {
        return serviceTraining.getTrainingsByQuizId(quizId);
    }


    @GetMapping("/courses/{courseId}")
    public ResponseEntity<List<Training>> getTrainingsForCourse(@PathVariable Long courseId) {
        List<Training> trainings = serviceTraining.getTrainingsForCourse(courseId);
        return ResponseEntity.ok(trainings);
    }


    @GetMapping("/is-enrolled/{userId}/{trainingId}")
    public ResponseEntity<Boolean> isUserEnrolled(@PathVariable Long userId, @PathVariable Long trainingId) {
        boolean isEnrolled = serviceTraining.isUserEnrolled(userId, trainingId);
        return ResponseEntity.ok(isEnrolled);
    }


    @GetMapping("/latest-trainings")
    public ResponseEntity<List<Training>> getLatestTrainings() {
        return ResponseEntity.ok(serviceTraining.getLatestTrainings());
    }

    @GetMapping("/not-enrolled/{userId}")
    public ResponseEntity<List<Training>> getTrainingsNotEnrolled(@PathVariable Long userId) {
        return ResponseEntity.ok(serviceTraining.getTrainingsNotEnrolled(userId));
    }

    @GetMapping("/is-eligible-for-discount/{userId}")
    public ResponseEntity<String> isUserEligibleForDiscount(@PathVariable Long userId) {
        String promoCode = serviceTraining.isEligibleForDiscount(userId);

        if (promoCode != null) {
            return ResponseEntity.ok(promoCode);  // ✅ Retourne le code promo s'il est généré
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Not eligible for a discount");
        }
    }
    @GetMapping("/validate-promo/{userId}/{enteredCode}")
    public ResponseEntity<Boolean> validateUserPromo(@PathVariable Long userId, @PathVariable String enteredCode) {
        boolean isValid = serviceTraining.validatePromoCode(userId, enteredCode);

        if (isValid) {
            return ResponseEntity.ok(true);  // ✅ Code promo valide
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // ❌ Code promo incorrect
        }
    }
    @GetMapping("/revenue")
    public List<Map<String, Object>> getTrainingRevenue() {
        return serviceTraining.getTrainingRevenue();
    }

    // ✅ API pour récupérer le revenu par heure
    @GetMapping("/revenue-by-hour")
    public List<Map<String, Object>> getRevenueByHour() {
        return serviceTraining.getRevenueByHour();
    }

    @GetMapping("/predicted-hourly-revenue")
    public double getPredictedHourlyRevenue() {
        return serviceTraining.predictFutureHourlyRevenue();
    }



}
