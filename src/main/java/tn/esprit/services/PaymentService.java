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
        // Récupérer l'utilisateur et vérifier son rôle
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé !"));
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("❌ Seuls les étudiants peuvent payer un training !");
        }

        // Récupérer la formation
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("❌ Formation non trouvée !"));

        // Création des paramètres de la session Checkout
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT) // Paiement unique
                .setSuccessUrl("http://localhost:4200/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount((long) (training.getPrice() * 100)) // Prix en cents
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
                // Ajouter des métadonnées pour récupérer les IDs après paiement
                .putMetadata("userId", userId.toString())
                .putMetadata("trainingId", trainingId.toString())
                .build();

        // Créer la session Stripe
        Session session = Session.create(params);

        // Construire la réponse avec l'ID et l'URL de la session
        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        response.put("url", session.getUrl());

        return response;
    }

    /**
     * ✅ Confirmation du paiement après succès (via redirection ou webhook)
     */
    public void confirmPayment(String sessionId) throws StripeException {
        // Récupérer la session Stripe pour vérifier le statut
        Session session = Session.retrieve(sessionId);
        if (!"paid".equals(session.getPaymentStatus())) {
            throw new RuntimeException("❌ Paiement non confirmé par Stripe !");
        }

        // Récupérer les métadonnées
        Long userId = Long.parseLong(session.getMetadata().get("userId"));
        Long trainingId = Long.parseLong(session.getMetadata().get("trainingId"));

        // Récupérer l'utilisateur et la formation
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé !"));
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("❌ Training non trouvé !"));

        // Vérifier le rôle (par sécurité)
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("❌ Seuls les étudiants peuvent être associés à un training !");
        }

        // Enregistrer le paiement
        Payment payment = new Payment();
        payment.setAmount(training.getPrice());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(session.getPaymentIntent()); // PaymentIntent ID
        payment.setReceiptUrl(session.getUrl());
        payment.setTrainingS(new HashSet<>());
        payment.getTrainingS().add(training);

        // Associer l'utilisateur au training
        user.getTrainings().add(training);
        training.getUsers().add(user);

        // Sauvegarder les changements
        paymentRepository.save(payment);
        userRepository.save(user);
        trainingRepository.save(training);
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