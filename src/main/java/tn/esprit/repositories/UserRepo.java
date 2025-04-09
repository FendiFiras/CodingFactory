package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByAssignment(Assignment assignment);

    User findByEvaluation(Evaluation evaluation);

    @Query("SELECT u.applications FROM User u WHERE u.idUser = :userId")
    Set<Application> findApplicationsByUserId(@Param("userId") Long userId);



}
