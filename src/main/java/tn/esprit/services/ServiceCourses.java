package tn.esprit.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Courses;
import tn.esprit.entities.Training;
import tn.esprit.repositories.CoursesRepo;
import tn.esprit.repositories.TrainingRepository;

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
}
