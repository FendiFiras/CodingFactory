package tn.esprit.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.exception.ApiException;

@Service
public class SmsService {

    @Value("${twilio.service_sid}")
    private String serviceSid;

    /**
     * Envoie un OTP via SMS
     * @param phoneNumber Numéro de téléphone
     * @return Statut de l'OTP (pending si OK, error sinon)
     */
    public String sendOtp(String phoneNumber) {
        try {
            String formattedNumber = formatPhoneNumber(phoneNumber);
            Verification verification = Verification.creator(serviceSid, formattedNumber, "sms").create();
            System.out.println("📩 OTP envoyé à : " + formattedNumber + " | Statut : " + verification.getStatus());
            return verification.getStatus();  // Retourne "pending" si l'OTP est envoyé
        } catch (ApiException e) {
            System.err.println("❌ Twilio API Error: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'OTP : " + e.getMessage());
            return "error";
        }
    }

    /**
     * Vérifie l'OTP entré par l'utilisateur
     * @param phoneNumber Numéro de téléphone
     * @param code Code OTP
     * @return true si valide, false sinon
     */
    public boolean verifyOtp(String phoneNumber, String code) {
        try {
            String formattedNumber = formatPhoneNumber(phoneNumber);

            VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                    .setTo(formattedNumber)  // Le numéro doit être au format international
                    .setCode(code)
                    .create();

            return "approved".equalsIgnoreCase(verificationCheck.getStatus());

        } catch (ApiException e) {
            System.err.println("❌ Twilio API Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la vérification OTP:");
            System.err.println("- Numéro: " + phoneNumber);
            System.err.println("- Code: " + code);
            System.err.println("- Service SID: " + serviceSid);
            System.err.println("- Erreur: " + e.getMessage());
        }

        return false;
    }

    /**
     * Formate le numéro de téléphone en format international
     * @param phone Numéro brut
     * @return Numéro formaté en "+216xxxxxxx"
     */
    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Numéro de téléphone invalide.");
        }

        phone = phone.replaceAll("[^0-9]", ""); // Supprime tout sauf les chiffres

        if (phone.length() == 8) {
            return "+216" + phone;
        } else if (phone.startsWith("216") && phone.length() == 11) {
            return "+" + phone;
        } else if (phone.startsWith("+216") && phone.length() == 12) {
            return phone;
        } else {
            throw new IllegalArgumentException("Format de numéro invalide: " + phone);
        }
    }
}
