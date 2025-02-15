package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Response;

@Repository
public interface ResponseREpo extends JpaRepository<Response, Long> {
}
