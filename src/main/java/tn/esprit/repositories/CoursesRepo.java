package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Courses;
@Repository
public interface CoursesRepo    extends JpaRepository<Courses,Long> {
}
