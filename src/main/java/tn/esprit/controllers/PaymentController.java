package tn.esprit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Training;
import tn.esprit.services.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(
            @RequestParam Long userId,
            @RequestParam Long trainingId) throws Exception {
        Map<String, String> session = paymentService.createStripeSession(userId, trainingId);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam("session_id") String sessionId) throws Exception {
        paymentService.confirmPayment(sessionId);
        return ResponseEntity.ok("Paiement réussi et inscription confirmée");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> handleCancel() {
        return ResponseEntity.ok("Paiement annulé");
    }

    // Optionnel : pour les paiements récurrents
    @PostMapping("/create-recurring-checkout-session")
    public ResponseEntity<Map<String, String>> createRecurringCheckoutSession(
            @RequestParam Long userId,
            @RequestParam Long trainingId,
            @RequestParam String priceId) throws Exception {
        Map<String, String> session = paymentService.createRecurringStripeSession(userId, trainingId, priceId);
        return ResponseEntity.ok(session);
    }




}