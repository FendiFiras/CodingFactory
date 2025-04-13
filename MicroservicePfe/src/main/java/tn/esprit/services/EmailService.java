package tn.esprit.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Evaluation;
import tn.esprit.entities.Offer;
import tn.esprit.entities.User;

// 1. Email Service Component

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendAssignmentNotification(User user, Offer offer, Assignment assignment) {
        try {
            // Add null checks
            if(user == null || user.getEmail() == null) {
                logger.error("Invalid user for email notification");
                return;
            }

            logger.info("Attempting to send email to: {}", user.getEmail());

            Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("offer", offer);
            context.setVariable("assignment", assignment);

            // Verify template resolution
            logger.debug("Using template: emails/assignment-notification");
            String process = templateEngine.process("emails/assignment-notification", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Verify email parameters
            helper.setTo(user.getEmail());
            helper.setSubject("New Assignment Notification");
            helper.setText(process, true);

            // Add CC for testing
            helper.setCc("your-email@domain.com");

            mailSender.send(message);
            logger.info("Email sent successfully to {}", user.getEmail());

        } catch (MessagingException e) {
            logger.error("Email sending failed: {}", e.getMessage());
            // Add detailed logging
            logger.debug("Full error stack: ", e);
        } catch (Exception e) {
            logger.error("General email error: {}", e.getMessage());
        }
    }

    public void sendEvaluationEmail(String studentEmail, String studentName, float score, String comment, byte[] pdfAttachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(studentEmail);
            helper.setSubject("Evaluation Results - " + score + "/5");

            // Create email content
            Context context = new Context();
            context.setVariable("studentName", studentName);
            context.setVariable("score", score);
            context.setVariable("comment", comment);

            String htmlContent = templateEngine.process("emails/evaluation-notification", context);
            helper.setText(htmlContent, true);

            // Add PDF attachment
            if(pdfAttachment != null) {
                helper.addAttachment("Evaluation_Report.pdf",
                        new ByteArrayResource(pdfAttachment),
                        "application/pdf");
            }

            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Failed to send evaluation email to {}: {}", studentEmail, e.getMessage());
        }
    }
}
