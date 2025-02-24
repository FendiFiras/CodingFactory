package tn.esprit.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.entities.Courses;
import tn.esprit.entities.Training;
import tn.esprit.repositories.CoursesRepo;
import tn.esprit.repositories.TrainingRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor

public class ServiceCourses implements IServiceCourses {

    TrainingRepository trainingRepo;
    CoursesRepo coursesRepo;

    public Courses addCourse(Courses course, Long trainingId) {
        // Trouver la formation associée à partir de son ID
        Training training = trainingRepo.findById(trainingId).orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + trainingId));

        // Associer la formation à ce cours
        course.setTraining(training);

        // Sauvegarder le cours dans la base de données
        return coursesRepo.save(course);
    }
    public Courses updateCourse(Courses course) {
        // Recherche du cours existant par son ID
        Courses existingCourse = coursesRepo.findById(course.getCourseId()).orElse(null);
        if (existingCourse == null) {
            throw new IllegalArgumentException("Course not found with ID: " + course.getCourseId());
        }

        // Mise à jour des attributs du cours existant
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setCourseDescription(course.getCourseDescription());
        existingCourse.setDifficulty(course.getDifficulty());
        existingCourse.setFileUrls(course.getFileUrls());
        existingCourse.setTraining(course.getTraining()); // Associe une nouvelle formation si nécessaire

        // Sauvegarde du cours mis à jour
        return coursesRepo.save(existingCourse);
    }
    public void deleteCourse(Long courseId) {
        // Vérifier si le cours existe
        Courses existingCourse = coursesRepo.findById(courseId).orElse(null);
        if (existingCourse == null) {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }

        // Supprimer le cours
        coursesRepo.deleteById(courseId);
    }

    public Courses getOneById(Long courseId) {
        return coursesRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
    }

    public List<Courses> getAllCourses() {
        return coursesRepo.findAll();
    }


    public Courses saveFile(Long courseId, MultipartFile file) throws IOException {
        Courses course = coursesRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (file != null && !file.isEmpty()) {
            // 🔥 Nom unique pour éviter les conflits
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("C:/uploads/").resolve(fileName);

            // ✅ Créer le dossier si nécessaire
            Files.createDirectories(filePath.getParent());

            // 📂 Sauvegarde du fichier
            Files.copy(file.getInputStream(), filePath);

            // 🔗 Stocke uniquement l’URL d’accès au fichier dans la base de données
            course.setFileUrls("http://localhost:8089/Courses/" + fileName);
        }

        return coursesRepo.save(course);
    }
    public List<Courses> getCoursesByTraining(Long trainingId) {
        return coursesRepo.findCoursesByTrainingId(trainingId);
    }


}
