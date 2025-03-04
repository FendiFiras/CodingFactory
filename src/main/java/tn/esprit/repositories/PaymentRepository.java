package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
