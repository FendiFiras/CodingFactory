package tn.esprit.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ServiceMail {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ Méthode pour envoyer un email avec un code promo
    public void sendPromoEmail(String toEmail, String promoCode) {
        String subject = "🎉 Congratulations! You've Earned a Discount!";

        // ✅ Contenu HTML de l'email
        String htmlBody = "<div style='font-family: Arial, sans-serif; text-align: center; padding: 20px; background-color: #f8f9fa;'>"
                + "<h2 style='color: #ff6600;'>🎊 Congratulations! 🎊</h2>"
                + "<p style='font-size: 16px; color: #333;'>"
                + "You have successfully enrolled in 3 trainings! As a thank you, here is your exclusive <strong>30% discount</strong> code for your next purchase."
                + "</p>"
                + "<div style='font-size: 20px; font-weight: bold; background: #ff6600; color: white; padding: 10px; border-radius: 8px; display: inline-block;'>"
                + promoCode + "</div>"
                + "<p style='font-size: 16px; color: #333;'>Use this code at checkout to enjoy your discount! 🎁</p>"
                + "<p style='font-size: 14px; color: #777;'>Thank you for your trust and happy learning! 🚀</p>"
                + "</div>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // ✅ HTML format

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("❌ Failed to send promo email!", e);
        }
    }
}