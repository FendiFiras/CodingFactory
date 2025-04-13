package tn.esprit.services;

import tn.esprit.entities.Training;

import java.util.List;
import java.util.Map;

public interface IServiceTraining {

    Training addTraining(Training training, Long userId);
     void deleteTraining(Long trainingId) ;
     Training updateTraining(Training training);
     Training getOneById(Long id) ;
     List<Training> getAllTrainings();

    void affecterQuizTraining(Long trainingId, Long quizId);
    List<Training> getTrainingsByUser(Long userId);
    List<Training> getTrainingsByQuizId(Long quizId);
    List<Training> getTrainingsForCourse(Long courseId);
    boolean isUserEnrolled(Long userId, Long trainingId);
  List<Training> getLatestTrainings();
    List<Training> getTrainingsNotEnrolled(Long userId);
    String  isEligibleForDiscount(Long userId);
  //  String checkAndGeneratePromo(Long userId);
  boolean validatePromoCode(Long userId, String enteredCode);

    public List<Map<String, Object>> getTrainingRevenue();

    List<Map<String, Object>> getRevenueByHour();
    double predictFutureHourlyRevenue();

}
