package tn.esprit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.entities.Courses;
import tn.esprit.entities.Training;
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


    @PostMapping(value = "/add_courses/{trainingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Courses addCourse(
            @PathVariable Long trainingId,
            @RequestPart("course") Courses course,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException { // ✅ Ajout de `throws IOException`
        return serviceCourses.addCourse(course, trainingId, files);
    }





    @PutMapping(value = "/update_course", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Courses updateCourse(
            @RequestPart("course") Courses course,  // 📌 Directement un objet JSON
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // 📌 Fichiers ajoutés
            @RequestPart(value = "existingFiles", required = false) List<String> existingFiles // 📌 Fichiers conservés
    ) throws IOException {
        return serviceCourses.updateCourse(course, files, existingFiles);
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
    /**
    @GetMapping("/Courses/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("C:/uploads/").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }


**/
    @GetMapping("/training/{trainingId}/courses")
    public ResponseEntity<List<Courses>> getCoursesByTraining(@PathVariable Long trainingId) {
        List<Courses> courses = serviceCourses.getCoursesByTraining(trainingId);
        return ResponseEntity.ok(courses);
    }




    @GetMapping("/Courses/{filename}")
    public ResponseEntity<Resource> getFilee(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("C:/uploads/").resolve(filename).normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            // 📌 Détection automatique du type MIME
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // 📂 Type générique si inconnu
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }







}
