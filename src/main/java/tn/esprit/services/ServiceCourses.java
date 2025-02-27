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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class ServiceCourses implements IServiceCourses {

    TrainingRepository trainingRepo;
    CoursesRepo coursesRepo;
    private final FileStorageService fileStorageService;

    public Courses addCourse(Courses course, Long trainingId, List<MultipartFile> files) throws IOException {
        Training training = trainingRepo.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + trainingId));

        course.setTraining(training);

        // 📂 Sauvegarde des fichiers et stockage des URLs
        if (files != null && !files.isEmpty()) {
            List<String> fileUrls = fileStorageService.saveFiles(files);
            course.setFileUrls(fileUrls);
        }

        return coursesRepo.save(course);
    }
    public Courses updateCourse(Courses course, List<MultipartFile> files, List<String> existingFiles) throws IOException {
        Optional<Courses> existingCourseOpt = coursesRepo.findById(course.getCourseId());

        if (existingCourseOpt.isPresent()) {
            Courses existingCourse = existingCourseOpt.get();

            // ✅ Mise à jour des informations du cours
            existingCourse.setCourseName(course.getCourseName());
            existingCourse.setCourseDescription(course.getCourseDescription());
            existingCourse.setDifficulty(course.getDifficulty());

            // ✅ Supprimer les fichiers retirés par l’utilisateur
            if (existingFiles != null) {
                existingCourse.setFileUrls(existingFiles); // 📌 Ne garder que les fichiers restants
            } else {
                existingCourse.setFileUrls(new ArrayList<>()); // 📌 Aucun fichier restant
            }

            // ✅ Ajouter les nouveaux fichiers s'ils existent
            if (files != null && !files.isEmpty()) {
                List<String> newFileUrls = fileStorageService.saveFiles(files);
                existingCourse.getFileUrls().addAll(newFileUrls);
            }

            return coursesRepo.save(existingCourse);
        } else {
            throw new RuntimeException("❌ Course not found!");
        }
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


    public List<Courses> getCoursesByTraining(Long trainingId) {
        return coursesRepo.findCoursesByTrainingId(trainingId);
    }


}
