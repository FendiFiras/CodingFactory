package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Discussion;
import tn.esprit.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
