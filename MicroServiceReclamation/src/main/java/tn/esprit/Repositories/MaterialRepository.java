package tn.esprit.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.Entities.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
