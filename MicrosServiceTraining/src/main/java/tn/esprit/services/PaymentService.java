package tn.esprit.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Payment;
import tn.esprit.entities.Role;
import tn.esprit.entities.Training;
import tn.esprit.entities.User;
import tn.esprit.repositories.PaymentRepository;
import tn.esprit.repositories.TrainingRepository;
import tn.esprit.repositories.UserRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private  ServiceMail serviceMail;
    @Autowired
    private InvoiceService invoiceService; //

    /**
     * ✅ Initialisation de Stripe avec la clé API
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * ✅ Génération d'une session Stripe Checkout pour un paiement unique
     */
    public Map<String, String> createStripeSession(Long userId, Long trainingId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé !"));

        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("❌ Seuls les étudiants peuvent payer un training !");
        }

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("❌ Training non trouvé !"));

        // ✅ Vérifier si l'utilisateur est éligible à une réduction
        boolean isEligibleForDiscount = trainingRepository.countTrainingsByUserId(userId) >= 3;
        double discountRate = isEligibleForDiscount ? 0.3 : 0.0; // 30% de réduction si éligible
        long finalPrice = (long) ((1 - discountRate) * training.getPrice() * 100); // Prix final en cents

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(finalPrice) // Prix après réduction
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(training.getTrainingName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .setCustomerEmail(user.getEmail())
                .putMetadata("userId", userId.toString())
                .putMetadata("trainingId", trainingId.toString())
                .build();

        Session session = Session.create(params);

        // ✅ Retourner l'URL de paiement Stripe
        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        response.put("url", session.getUrl());
        return response;



    }


    /**
     * ✅ Confirmation du paiement après succès (via redirection ou webhook)
     */
    public void confirmPayment(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        if (!"paid".equals(session.getPaymentStatus())) {
            throw new RuntimeException("❌ Paiement non confirmé !");
        }

        Long userId = Long.parseLong(session.getMetadata().get("userId"));
        Long trainingId = Long.parseLong(session.getMetadata().get("trainingId"));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé !"));
        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new RuntimeException("❌ Training non trouvé !"));

        Payment payment = new Payment();
        payment.setAmount(training.getPrice());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(session.getPaymentIntent());
        payment.setReceiptUrl(session.getUrl());

        training.getPayments().add(payment);
        user.getTrainings().add(training);
        training.getUsers().add(user);

        paymentRepository.save(payment);
        userRepository.save(user);
        trainingRepository.save(training);

        // ✅ **Convertir la date**
        String formattedDate = payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        // ✅ **Générer la facture en PDF**
        byte[] pdfInvoice = invoiceService.generateInvoice(
                user.getFirstName() + " " + user.getLastName(),
                training.getTrainingName(),
                training.getPrice(),
                payment.getTransactionId(),
                formattedDate
        );

        // 📧 **Envoyer la facture PDF par email**
        serviceMail.sendInvoiceEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName(),
                training.getTrainingName(), training.getPrice(), payment.getTransactionId(), formattedDate);
    }
    /**
     * ✅ (Optionnel) Génération d'une session pour un paiement récurrent
     */
    public Map<String, String> createRecurringStripeSession(Long userId, Long trainingId, String priceId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé !"));
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("❌ Seuls les étudiants peuvent payer un training !");
        }

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("❌ Formation non trouvée !"));

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION) // Paiement récurrent
                .setSuccessUrl("http://localhost:4200/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(priceId) // ID de prix récurrent créé dans Stripe
                                .setQuantity(1L)
                                .build()
                )
                .setCustomerEmail(user.getEmail())
                .putMetadata("userId", userId.toString())
                .putMetadata("trainingId", trainingId.toString())
                .build();

        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        response.put("url", session.getUrl());

        return response;
    }



}