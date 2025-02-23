package tn.esprit.services;

import tn.esprit.entities.Training;

import java.util.List;

public interface IServiceTraining {

    Training addTraining(Training training, Long userId);
     void deleteTraining(Long trainingId) ;
     Training updateTraining(Training training);
     Training getOneById(Long id) ;
     List<Training> getAllTrainings();

    void affecterQuizTraining(Long trainingId, Long quizId);
    List<Training> getTrainingsByUser(Long userId);

}
