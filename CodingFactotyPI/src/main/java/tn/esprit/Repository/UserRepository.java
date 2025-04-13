package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Gender;
import tn.esprit.entities.Role;
import tn.esprit.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // Méthode personnalisée pour trouver un utilisateur par email
    List<User> findByRole(Role role);
    long countByGender(Gender gender);
    @Query("SELECT u.region, COUNT(u) FROM User u GROUP BY u.region")
    List<Object[]> countUsersByRegion();
}