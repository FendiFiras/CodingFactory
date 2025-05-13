package tn.esprit.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.Entities.Messages;

import java.util.List;

public interface MessageRepository extends JpaRepository<Messages, Long> {
    List<Messages> findByReclamation_IdReclamationOrderByTimestampAsc(Long idReclamation);

    @Query("SELECT DISTINCT m.reclamation.idReclamation FROM Messages m WHERE m.sender = 'user'")
    List<Long> findReclamationIdsWithUserMessages();
}
