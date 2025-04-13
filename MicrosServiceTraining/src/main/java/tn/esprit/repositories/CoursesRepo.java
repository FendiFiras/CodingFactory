package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Courses;

import java.util.List;

@Repository
public interface CoursesRepo    extends JpaRepository<Courses,Long> {

    @Query("SELECT c FROM Courses c WHERE c.training.trainingId = :trainingId")
    List<Courses> findCoursesByTrainingId(@Param("trainingId") Long trainingId);



}
