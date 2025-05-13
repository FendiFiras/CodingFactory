package tn.esprit.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.Entities.Reclamation;

import java.util.List;
import java.util.Map;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
  @Query("SELECT m.label AS label, SUM(r.quantity) AS totalQuantity " +
    "FROM Reclamation r JOIN r.materials m " +
    "GROUP BY m.label " +
    "ORDER BY totalQuantity DESC")
  List<Map<String, Object>> getMostReclamatedMaterials();
}
