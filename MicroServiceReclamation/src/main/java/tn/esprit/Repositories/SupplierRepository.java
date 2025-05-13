package tn.esprit.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.Entities.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
