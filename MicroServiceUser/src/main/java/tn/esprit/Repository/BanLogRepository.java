package tn.esprit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.entities.BanLog;

import java.util.List;

@Repository
public interface BanLogRepository extends JpaRepository<BanLog, Long> {
    List<BanLog> findByUser_IdUser(Long userId);
    @Query("SELECT b FROM BanLog b JOIN FETCH b.user")
    List<BanLog> findAllWithUser();


}