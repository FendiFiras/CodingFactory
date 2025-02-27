package tn.esprit.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.entities.Courses;

import java.io.IOException;
import java.util.List;

public interface IServiceCourses {

      //Courses addCourse(Courses course, Long trainingId);
      Courses addCourse(Courses course, Long trainingId, List<MultipartFile> files) throws IOException;
      //Courses updateCourse(Courses course, List<MultipartFile> files) throws IOException;
      void deleteCourse(Long courseId);
      Courses getOneById(Long courseId);
      List<Courses> getAllCourses();
    //  Courses saveFile(Long courseId, MultipartFile file) throws IOException;

      Courses updateCourse(Courses course, List<MultipartFile> files, List<String> existingFiles) throws IOException;

}
