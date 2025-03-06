package tn.esprit.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class ServiceMail {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private InvoiceService invoiceService;

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

    // ✅ Envoyer une facture par email (Mise en page améliorée)
    public void sendInvoiceEmail(String toEmail, String studentName, String trainingName,
                                 double amountPaid, String transactionId, String paymentDate) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // ✅ Générer le PDF
            byte[] pdfBytes = invoiceService.generateInvoice(studentName, trainingName, amountPaid, transactionId, paymentDate);

            // ✅ Définir l'email
            helper.setTo(toEmail);
            helper.setSubject("📄 Training Payment Invoice");
            helper.setText(getStyledEmailTemplate(studentName, trainingName, amountPaid, transactionId, paymentDate), true);

            // ✅ Ajouter la facture PDF en pièce jointe
            helper.addAttachment("Invoice.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);
            System.out.println("📧 Email envoyé avec succès à " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("❌ Erreur lors de l'envoi de l'email !", e);
        }
    }

    // ✅ **Template HTML identique à `sendPromoEmail` avec un design amélioré**
    private String getStyledEmailTemplate(String studentName, String trainingName, double amountPaid,
                                          String transactionId, String paymentDate) {
        return "<div style='font-family: Arial, sans-serif; text-align: center; padding: 20px; background-color: #f8f9fa;'>"
                + "<h2 style='color: #ff6600;'>📄 Training Payment Invoice</h2>"
                + "<p style='font-size: 16px; color: #333;'>Dear <strong>" + studentName + "</strong>,</p>"
                + "<p style='font-size: 16px; color: #333;'>"
                + "Thank you for your payment for the training <strong>" + trainingName + "</strong>.</p>"
                + "<div style='background-color: white; padding: 15px; border-radius: 10px; display: inline-block; text-align: left; box-shadow: 0px 0px 10px rgba(0,0,0,0.1);'>"
                + "<p style='font-size: 18px;'><strong>💰 Amount Paid:</strong> $" + amountPaid + "</p>"
                + "<p style='font-size: 18px;'><strong>🔢 Transaction ID:</strong> " + transactionId + "</p>"
                + "<p style='font-size: 18px;'><strong>📅 Payment Date:</strong> " + paymentDate + "</p>"
                + "</div>"
                + "<p style='font-size: 16px; color: #333;'>You will find your invoice attached. 📎</p>"
                + "<p style='font-size: 14px; color: #777;'>Best regards,<br><strong>Training Platform Team</strong> 🚀</p>"
                + "</div>";
    }



}