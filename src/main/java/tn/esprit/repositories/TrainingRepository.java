package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Training;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t JOIN t.Users u WHERE u.idUser = :userId")
    List<Training> findTrainingsByUserId(@Param("userId") Long userId);

}



