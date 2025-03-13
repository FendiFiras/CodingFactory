package tn.esprit.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAcceptedEmail(String toEmail, String userName, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Encodage UTF-8

        helper.setTo(toEmail);
        helper.setSubject("🎉 Welcome Aboard! Your Instructor Account Has Been Approved 🎉");

        String htmlContent = String.format(
                "<html>" +
                        "<head>" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                        "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "        .header { text-align: center; padding: 20px 0; }" +
                        "        .header h1 { color: #333333; font-size: 28px; margin-bottom: 10px; }" +
                        "        .content { padding: 20px; }" +
                        "        .content h2 { color: #333333; font-size: 24px; margin-bottom: 20px; }" +
                        "        .content p { color: #555555; font-size: 16px; line-height: 1.6; }" +
                        "        .credentials { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
                        "        .credentials p { margin: 5px 0; }" +
                        "        .footer { text-align: center; padding: 20px 0; color: #888888; font-size: 14px; }" +
                        "        .footer a { color: #007BFF; text-decoration: none; }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='container'>" +
                        "        <div class='header'>" +
                        "            <h1>Welcome to The Coding Factory! &#127881;</h1>" +
                        "        </div>" +
                        "        <div class='content'>" +
                        "            <p>Dear <strong>%s</strong>,</p>" +
                        "            <p>We are thrilled to inform you that your application to become an instructor has been approved! &#127891;</p>" +
                        "            <p>Your dedication and expertise are highly valued, and we are excited to have you join our team of professionals. &#128188;</p>" +
                        "            <div class='credentials'>" +
                        "                <p>Here are your login credentials:</p>" +
                        "                <p><strong>&#128231; Email:</strong> %s</p>" +
                        "                <p><strong>&#128273; Password:</strong> %s</p>" +
                        "            </div>" +
                        "            <p>Please log in to your account to complete your profile and start creating impactful content. &#128640;</p>" +
                        "            <p>If you have any questions or need assistance, feel free to reach out to our support team at <a href='mailto:supportCodingFactory@CodingFactory.com'>support@example.com</a>. &#128232;</p>" +
                        "            <p>Once again, welcome to the team! We look forward to seeing the great work you will accomplish. &#127775;</p>" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            <p>Best regards,<br>The Coding Factory Team</p>" +
                        "            <p><a href='https://www.codingfactory.com'>Visit our website</a></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>", userName, toEmail, password);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
    public void sendRejectionEmail(String toEmail, String userName, String rejectionReason) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("❌ Your Instructor Application Has Been Reviewed");

        String htmlContent = String.format(
                "<html>" +
                        "<head>" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                        "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "        .header { text-align: center; padding: 20px 0; }" +
                        "        .header h1 { color: #333333; font-size: 28px; margin-bottom: 10px; }" +
                        "        .content { padding: 20px; }" +
                        "        .content h2 { color: #333333; font-size: 24px; margin-bottom: 20px; }" +
                        "        .content p { color: #555555; font-size: 16px; line-height: 1.6; }" +
                        "        .footer { text-align: center; padding: 20px 0; color: #888888; font-size: 14px; }" +
                        "        .footer a { color: #007BFF; text-decoration: none; }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='container'>" +
                        "        <div class='header'>" +
                        "            <h1>Application Status: Not Approved &#128532;</h1>" +
                        "        </div>" +
                        "        <div class='content'>" +
                        "            <p>Dear <strong>%s</strong>,</p>" +
                        "            <p>We regret to inform you that your application to become an instructor at The Coding Factory has not been approved at this time. &#128533;</p>" +
                        "            <p><strong>Reason for rejection:</strong> %s</p>" +
                        "            <p>We appreciate the time and effort you invested in your application and encourage you to reapply in the future if your circumstances change. &#128522;</p>" +
                        "            <p>If you have any questions or would like feedback on your application, please feel free to contact us at <a href='mailto:supportCodingFactory@CodingFactory.com'>support@example.com</a>. &#128232;</p>" +
                        "            <p>Thank you for your interest in joining our team, and we wish you the best in your future endeavors. &#127881;</p>" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            <p>Best regards,<br>The Coding Factory Team</p>" +
                        "            <p><a href='https://www.codingfactory.com'>Visit our website</a></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>", userName, rejectionReason);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
    public void sendOTPEmail(String toEmail, String otp) {
        String subject = "Almost there! Enter your OTP";
        String htmlContent = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; padding: 20px; }" +
                ".container { max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }" +
                ".header { background-color: #FF6F00; color: #fff; padding: 10px; border-radius: 5px; text-align: center; }" +
                ".footer { font-size: 12px; color: #888; text-align: center; margin-top: 20px; }" +
                ".otp { font-size: 24px; font-weight: bold; color: #FF6F00; text-align: center; padding: 10px; margin-top: 20px; background-color: #f9f9f9; border: 1px solid #ddd; border-radius: 10px; }" + // Ajout de border-radius
                ".footer a { color: #FF6F00; text-decoration: none; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Almost there</h2>" +
                "</div>" +
                "<p>Enter the following code on the login page you just used:</p>" +
                "<div class='otp'>" +
                "<p>" + otp + "</p>" +
                "</div>" +
                "<p>This code will be active for 30 minutes. If you don't make it in time, don't sweat it, you can always request a new one.</p>" +
                "<p>If you weren't expecting this email, someone else may have entered your email address by accident.</p>" +
                "<p>Questions? Our friendly support team is always happy to help.</p>" +
                "<div class='footer'>" +
                "<p>Beautifully obvious tools: Coding Factory, Collect, and WePresent.</p>" +
                "<p><a href='https://www.codingfactory.com'>Help</a> | <a href='https://www.codingfactory.com/about'>About us</a> | <a href='https://www.codingfactory.com/legal'>Legal</a></p>" +
                "<p>To make sure our emails arrive, please add <b>noreply@CodingFactory.com</b> to your contact.</p>" +
                "<p>Tunisia, 1080, Tunis</p>" + // Ajout de la localisation en Tunisie
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}