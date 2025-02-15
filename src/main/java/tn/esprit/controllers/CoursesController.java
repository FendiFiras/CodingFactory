package tn.esprit.controllers;

import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Courses;
import tn.esprit.services.ServiceCourses;

import java.util.List;

@RestController
@RequestMapping("/Courses")
public class CoursesController {


@Autowired
private ServiceCourses serviceCourses;
    @PostMapping("/add_courses")
    public Courses addCourse(@RequestBody Courses course, @RequestParam Long trainingId) {
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


}
