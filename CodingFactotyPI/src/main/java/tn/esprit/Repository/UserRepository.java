package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.Role;
import tn.esprit.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // Méthode personnalisée pour trouver un utilisateur par email
    List<User> findByRole(Role role);
    User findByPassword(String password);
}