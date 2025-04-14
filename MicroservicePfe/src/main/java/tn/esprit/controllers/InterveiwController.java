package tn.esprit.controllers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Application;
import tn.esprit.entities.Interview;
import tn.esprit.entities.User;
import tn.esprit.repositories.ApplicationRepository;
import tn.esprit.repositories.UserRepo;
import tn.esprit.repositories.interveiwRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/interview")
public class InterveiwController {


    private final interveiwRepo interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final JavaMailSender mailSender;
    private final UserRepo userRepository;
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Interview>> getInterviewsByStudent(@PathVariable Long studentId) {
        List<Interview> interviews = interviewRepository.findInterviewsByStudentId(studentId);
        return ResponseEntity.ok(interviews);
    }
    @PostMapping
    public ResponseEntity<?> scheduleInterview(
            @RequestParam Long applicationId,
            @RequestParam String interviewDate,
            @RequestParam String interviewTime) {

        try {
            // Parse date/time
            LocalDate parsedDate = LocalDate.parse(interviewDate);
            LocalTime parsedTime = LocalTime.parse(interviewTime);

            // Check for existing interview
            if (interviewRepository.existsByApplicationId(applicationId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Interview already scheduled.");
            }

            // Find application
            Application app = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Save interview
            Interview interview = new Interview();
            interview.setApplication(app);
            interview.setInterviewDate(parsedDate);
            interview.setInterviewTime(parsedTime);
            interviewRepository.save(interview);

            // Find student
            User student = userRepository.findUserByApplicationId(applicationId);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student not found.");
            }

            // Validate email
            if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Student email is missing.");
            }

            // Send email
            sendInterviewEmail(student.getEmail(), parsedDate, parsedTime);
            return ResponseEntity.ok("Interview scheduled.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }


    private void sendInterviewEmail(String to, LocalDate interviewDate, LocalTime interviewTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Interview Confirmation");

            String formattedDate = interviewDate.toString();
            String formattedTime = interviewTime.toString();

            String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: 'Segoe UI', Tahoma, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 20px auto; padding: 20px; }
                    .header { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                    .details { margin: 25px 0; }
                    .footer { margin-top: 30px; font-size: 0.9em; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>TechCorp International</h2>
                    </div>
                    
                    <p>Dear Candidate,</p>
                    
                    <div class="details">
                        <p>We are pleased to confirm your upcoming interview:</p>
                        
                        <table>
                            <tr>
                                <td><strong>Date:</strong></td>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <td><strong>Time:</strong></td>
                                <td>%s</td>
                            </tr>
                           
                        </table>
                    </div>

                    <p>Please bring:</p>
                    <ul>
                        <li>Government-issued ID</li>
                        <li>Copies of your credentials</li>
                        <li>List of professional references</li>
                    </ul>

                    <div class="footer">
                        <p>Best regards,<br>
                        Hiring Team</p>
                       
                    </div>
                </div>
            </body>
            </html>
            """.formatted(formattedDate, formattedTime);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
