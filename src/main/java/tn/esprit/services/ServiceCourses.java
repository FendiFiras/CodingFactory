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
    public Courses updateCourse(Courses course, List<MultipartFile> files) throws IOException {
        Optional<Courses> existingCourse = coursesRepo.findById(course.getCourseId());

        if (existingCourse.isPresent()) {
            Courses updatedCourse = existingCourse.get();
            updatedCourse.setCourseName(course.getCourseName());
            updatedCourse.setCourseDescription(course.getCourseDescription());
            updatedCourse.setDifficulty(course.getDifficulty());

            // ✅ Gérer les fichiers
            if (files != null && !files.isEmpty()) {
                List<String> fileUrls = fileStorageService.saveFiles(files);
                updatedCourse.getFileUrls().addAll(fileUrls);
            }

            return coursesRepo.save(updatedCourse);
        } else {
            throw new RuntimeException("Course not found!");
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
