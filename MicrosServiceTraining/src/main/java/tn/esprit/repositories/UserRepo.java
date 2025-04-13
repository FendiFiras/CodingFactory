package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {


}
