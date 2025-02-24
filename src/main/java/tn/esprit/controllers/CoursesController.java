package tn.esprit.controllers;

import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.entities.Courses;
import tn.esprit.services.ServiceCourses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/Courses")

public class CoursesController {

    private static final String UPLOAD_DIR = "C:/uploads/";

@Autowired
private ServiceCourses serviceCourses;
    @PostMapping("/add_courses/{trainingId}")
    public Courses addCourse(@RequestBody Courses course, @PathVariable Long trainingId) {
        return serviceCourses.addCourse(course, trainingId);
    }
    @PutMapping("/updateCourse")
    public Courses updateCourse(@RequestBody Courses course) {
        return serviceCourses.updateCourse(course);
    }

    @DeleteMapping("/deleteCourse/{idC}")
    public void deleteCourse(@PathVariable("idC") Long courseId) {
        serviceCourses.deleteCourse(courseId);
    }
    @GetMapping("/getCourse/{id}")
    public Courses getOneById(@PathVariable("id") Long courseId) {
        return serviceCourses.getOneById(courseId);
    }
    @GetMapping("/getAllCourses")
    public List<Courses> getAllCourses() {
        return serviceCourses.getAllCourses();
    }
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("C:/uploads/").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/uploadFile/{courseId}")
    public ResponseEntity<Courses> uploadFile(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file) throws IOException {

        Courses course = serviceCourses.getOneById(courseId);

        // Vérifier si le fichier est bien présent
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("C:/uploads/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 🔥 Stocker l’URL du fichier dans la base de données
            course.setFileUrls("http://localhost:8089/uploads/" + fileName);
            serviceCourses.updateCourse(course);
        }

        return ResponseEntity.ok(course);
    }
    @GetMapping("/training/{trainingId}/courses")
    public ResponseEntity<List<Courses>> getCoursesByTraining(@PathVariable Long trainingId) {
        List<Courses> courses = serviceCourses.getCoursesByTraining(trainingId);
        return ResponseEntity.ok(courses);
    }


}
