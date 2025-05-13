package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.entities.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT HOUR(p.paymentDate) as hour, SUM(p.amount) as totalRevenue, AVG(p.amount) as avgRevenue " +
            "FROM Payment p GROUP BY HOUR(p.paymentDate) ORDER BY HOUR(p.paymentDate)")
    List<Object[]> getRevenueGroupedByHour();
    @Query("SELECT DATE(p.paymentDate), SUM(p.amount) " +
            "FROM Payment p GROUP BY DATE(p.paymentDate) ORDER BY DATE(p.paymentDate)")
    List<Object[]> getDailyRevenue();

}
