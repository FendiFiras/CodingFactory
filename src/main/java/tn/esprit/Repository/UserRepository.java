package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {




}
