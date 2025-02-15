package tn.esprit.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Role;
import tn.esprit.entities.Training;
import tn.esprit.entities.User;
import tn.esprit.repositories.TrainingRepository;
import tn.esprit.repositories.UserRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor

public class ServiceTraining  implements IServiceTraining {


    UserRepo userRepo;
    TrainingRepository trainingRepo;

    public Training addTraining(Training training, Long userId) {
        Set<User> validUsers = new HashSet<>(); // Crée un Set pour stocker un seul utilisateur valide.

        Optional<User> userOpt = userRepo.findById(userId); // Utilisation de Optional<User>
        if (userOpt.isPresent()) {
            User user = userOpt.get(); // Récupère l'utilisateur
            if (user.getRole().equals(Role.INSTRUCTOR)) { // Vérification du rôle
                validUsers.add(user); // Ajoute l'utilisateur au Set des utilisateurs valides.
            }
        }

        training.setUsers(validUsers); // Associe l'utilisateur valide au Training.
        return trainingRepo.save(training); // Sauvegarde le Training, la table de jointure sera mise à jour automatiquement.
    }


    public void deleteTraining(Long trainingId) {
        // Vérifier si le training existe
        if (!trainingRepo.existsById(trainingId)) {
            throw new IllegalArgumentException("Training not found with ID: " + trainingId);
        }

        // Récupérer le training
        Training training = trainingRepo.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + trainingId));

        // Dissocier les utilisateurs de cette formation
        Set<User> usersInTraining = training.getUsers();
        for (User user : usersInTraining) {
            user.getTrainings().remove(training);  // Retirer cette formation de l'utilisateur
            userRepo.save(user);  // Sauvegarder les changements dans l'utilisateur
        }

        // Supprimer la formation
        trainingRepo.deleteById(trainingId);
    }
    public Training updateTraining(Training training) {
        // Vérifier si le Training existe déjà dans la base de données
        Optional<Training> existingTrainingOpt = trainingRepo.findById(training.getTrainingId());
        if (existingTrainingOpt.isPresent()) {
            Training existingTraining = existingTrainingOpt.get();

            // Mettre à jour les attributs du Training existant avec les nouveaux attributs
            existingTraining.setTrainingName(training.getTrainingName());
            existingTraining.setStartDate(training.getStartDate());
            existingTraining.setEndDate(training.getEndDate());
            existingTraining.setPrice(training.getPrice());
            existingTraining.setType(training.getType());

            // Mettez à jour les utilisateurs associés si nécessaire
            existingTraining.setUsers(training.getUsers()); // Cela associe les utilisateurs à la formation mise à jour

            // Sauvegarder la formation mise à jour dans la base de données
            return trainingRepo.save(existingTraining);
        } else {
            throw new IllegalArgumentException("Training not found with ID: " + training.getTrainingId());
        }
    }

    public Training getOneById(Long id) {
        Training training = trainingRepo.findById(id).orElse(null);  // Recherche le training, renvoie null si non trouvé
        if (training == null) {
            throw new IllegalArgumentException("Training not found with ID: " + id);
        }
        return training;
    }
    public List<Training> getAllTrainings() {
        return trainingRepo.findAll(); // Récupère toutes les formations de la base de données
    }

}