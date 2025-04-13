package tn.esprit.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Quiz;
import tn.esprit.entities.Role;
import tn.esprit.entities.Training;
import tn.esprit.entities.User;
import tn.esprit.repositories.PaymentRepository;
import tn.esprit.repositories.QuizRepo;
import tn.esprit.repositories.TrainingRepository;
import tn.esprit.repositories.UserRepo;
import tn.esprit.entities.Payment;

import java.util.*;
import java.security.SecureRandom;

@Service
@AllArgsConstructor

public class ServiceTraining  implements IServiceTraining {


    UserRepo userRepo;
    TrainingRepository trainingRepo;
    QuizRepo quizRepo;
    PaymentRepository paymentRepo;
    private final Map<Long, String> promoCodeCache = new HashMap<>(); // ✅ Cache pour stocker les codes promos générés
    @Autowired
    private ServiceMail serviceMail; // ✅ Injecter le service mail
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
    public void affecterQuizTraining(Long trainingId, Long quizId) {
        // Récupérer le training par son ID
        Training training = trainingRepo.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + trainingId));

        // Récupérer le quiz par son ID
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId));

        // Associer le quiz au training
        training.setQuiz(quiz);

        // Sauvegarder le training mis à jour
        trainingRepo.save(training);
    }
    // ✅ Récupérer les formations d'un utilisateur spécifique
    public List<Training> getTrainingsByUser(Long userId) {
        return trainingRepo.findTrainingsByUserId(userId);
    }


    // ✅ Récupérer les formations associées à un quiz
    public List<Training> getTrainingsByQuizId(Long quizId) {
        return trainingRepo.findTrainingsByQuizId(quizId);
    }

    public List<Training> getTrainingsForCourse(Long courseId) {
        return trainingRepo.findTrainingsByCourseId(courseId);
    }

    public boolean isUserEnrolled(Long userId, Long trainingId) {
        // Vérifier si l'utilisateur existe
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé !"));

        // Vérifier si la formation existe
        Training training = trainingRepo.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("❌ Formation non trouvée !"));

        // Vérifier si l'utilisateur est déjà inscrit à cette formation
        return user.getTrainings().contains(training);
    }
    public List<Training> getLatestTrainings() {
        return trainingRepo.findTop5ByOrderByStartDateDesc();
    }

    public List<Training> getTrainingsNotEnrolled(Long userId) {
        return trainingRepo.findTrainingsNotEnrolledByUser(userId);
    }
    public String isEligibleForDiscount(Long userId) {
        long trainingCount = trainingRepo.countTrainingsByUserId(userId);
        System.out.println("📌 Nombre de trainings achetés par l'utilisateur " + userId + " : " + trainingCount);

        if (trainingCount >= 3) {
            String promoCode = generatePromoCodeForUser(userId); // ✅ Génère un nouveau code à chaque fois

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("❌ User not found"));

            System.out.println("📩 Tentative d'envoi d'un e-mail à : " + user.getEmail() + " avec le code : " + promoCode);

            try {
                serviceMail.sendPromoEmail(user.getEmail(), promoCode);
                promoCodeCache.put(userId, promoCode); // ✅ Mettre à jour le cache avec le dernier code
                System.out.println("✅ E-mail envoyé avec succès !");
                return "Mail Sent"; // ✅ Retourne "Mail Sent" pour confirmer l'envoi
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
                e.printStackTrace();
                return "Mail Error"; // ❌ Indiquer que l'envoi a échoué
            }
        }

        return "Not Eligible";  // ❌ Si l'utilisateur n'a pas 3 trainings
    }




    public String generatePromoCodeForUser(Long userId) {
        return promoCodeCache.computeIfAbsent(userId, k -> {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            SecureRandom random = new SecureRandom();
            StringBuilder promoCode = new StringBuilder();

            for (int i = 0; i < 8; i++) {  // Générer un code promo unique de 8 caractères
                int index = random.nextInt(characters.length());
                promoCode.append(characters.charAt(index));
            }

            return promoCode.toString();
        });
    }


    public boolean validatePromoCode(Long userId, String enteredCode) {
        if (!promoCodeCache.containsKey(userId)) {
            return false;  // ❌ Aucune promo disponible pour cet utilisateur
        }

        String expectedPromoCode = promoCodeCache.get(userId); // 🔍 Récupérer le code promo généré pour cet utilisateur
        return expectedPromoCode.equalsIgnoreCase(enteredCode.trim());  // ✅ Comparer les codes
    }
    public List<Map<String, Object>> getTrainingRevenue() {
        List<Training> trainings = trainingRepo.findAll();
        List<Map<String, Object>> revenueData = new ArrayList<>();

        for (Training training : trainings) {
            double totalRevenue = training.getPayments().stream()
                    .mapToDouble(Payment::getAmount)
                    .sum();

            Map<String, Object> data = new HashMap<>();
            data.put("trainingName", training.getTrainingName());
            data.put("revenue", totalRevenue);
            revenueData.add(data);
        }
        return revenueData;
    }


    public List<Map<String, Object>> getRevenueByHour() {
        List<Object[]> results = paymentRepo.getRevenueGroupedByHour();

        List<Map<String, Object>> revenueByHour = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("hour", result[0]);  // Heure
            data.put("totalRevenue", result[1]);  // Revenu total
            data.put("avgRevenue", result[2]);  // Revenu moyen
            revenueByHour.add(data);
        }
        return revenueByHour;
    }



    public double predictFutureHourlyRevenue() {
        List<Object[]> results = paymentRepo.getRevenueGroupedByHour();
        int n = results.size();

        if (n < 2) return 0; // Pas assez de données pour une prédiction

        // Variables pour la régression linéaire
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            Number hourNumber = (Number) results.get(i)[0]; // Heure (Integer ou Double)
            Number revenueNumber = (Number) results.get(i)[1]; // Revenu (Integer ou Double)

            double hour = hourNumber.doubleValue(); // Convertir proprement en double
            double revenue = revenueNumber.doubleValue(); // Convertir proprement en double

            sumX += hour;
            sumY += revenue;
            sumXY += hour * revenue;
            sumX2 += hour * hour;
        }

        // Calcul des coefficients de la régression linéaire
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;

        // Prédiction du revenu pour l'heure suivante
        double nextHour = n + 1;
        return slope * nextHour + intercept;
    }



}