package tn.esprit.services;

import tn.esprit.entities.Courses;

import java.util.List;

public interface IServiceCourses {

      Courses addCourse(Courses course, Long trainingId);
      Courses updateCourse(Courses course);
      void deleteCourse(Long courseId);
      Courses getOneById(Long courseId);
      List<Courses> getAllCourses();
}
