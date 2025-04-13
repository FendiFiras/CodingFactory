package tn.esprit.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.entities.Assignment;
import tn.esprit.entities.Offer;
import tn.esprit.entities.User;
import tn.esprit.services.EmailService;

import java.time.LocalDate;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestEmailController {
    private final EmailService emailService;

    // Add test endpoint
    @GetMapping("/send-test-email")
    public String sendTestEmail() {
        User mockUser = new User();
        mockUser.setEmail("thesecsesfulali@gmail.com");
        mockUser.setLastName("Test User");

        Offer mockOffer = new Offer();
        mockOffer.setTitle("Test Position");

        Assignment mockAssignment = new Assignment();


        emailService.sendAssignmentNotification(mockUser, mockOffer, mockAssignment);
        return "Test email sent";
    }
}
