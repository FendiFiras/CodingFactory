package tn.esprit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.entities.Partnership;
import tn.esprit.entities.Offer;
import tn.esprit.entities.Application;
import tn.esprit.entities.User;
import tn.esprit.repositories.ApplicationRepository;
import tn.esprit.repositories.OfferRepository;
import tn.esprit.services.AiScoreService;
import tn.esprit.services.IPartnershipService;
import tn.esprit.services.IOfferService;
import tn.esprit.services.IApplicationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular app

@RequiredArgsConstructor
public class ApplicationController {
    private final IApplicationService applicationService;
    private final AiScoreService aiScoreService;
    private final ApplicationRepository applicationRepository;
    private final OfferRepository offerRepository;
    @PostMapping(value = "/apply/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Application> applyForOffer(
            @PathVariable Long userId,
            @RequestPart("application") String applicationJson,
            @RequestPart(value = "cvFile", required = false) MultipartFile cvFile) {
        try {
            // Parse the application JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Application application = objectMapper.readValue(applicationJson, Application.class);

            // Process the CV file (if provided)
            if (cvFile != null) {
                // Save the CV file or process it as needed
                // Example: String cvFilePath = saveFile(cvFile);
                // application.setCvFilePath(cvFilePath);
            }

            // Call the service method to apply for the offer
            Application createdApplication = applicationService.applyForOffer(application, userId);
            return new ResponseEntity<>(createdApplication, HttpStatus.CREATED);
        } catch (RuntimeException | IOException e) {
            // Handle exceptions and return an appropriate error response
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Set<Application>> getApplicationsByUserId(@PathVariable Long userId) {
        Set<Application> applications = applicationService.getApplicationsByStudent(userId);
        if (applications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/user/{applicationId}")
    public ResponseEntity<Long> getUserIdByApplicationId(@PathVariable Long applicationId) {
        try {
            Long userId = applicationService.getUserIdByApplicationId(applicationId);
            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/applicationsforCR/{offerId}")
    public ResponseEntity<List<Application>> getApplicationsByOfferId(@PathVariable Long offerId) {
        List<Application> applications = applicationService.getApplicationsByOfferId(offerId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/student/{userId}")
    public ResponseEntity<Set<Application>> getApplicationsByStudent(@PathVariable Long userId) {
        Set<Application> applications = applicationService.getApplicationsByStudent(userId);
        if (applications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(applications);

    }

    @CrossOrigin(origins = "http://localhost:4200")

    @GetMapping("/applicationswithscore")
    public List<Application> getApplicationsWithScores(@RequestParam Long offerId, @RequestParam String requiredSkills) {
        System.out.println("Received offerId: " + offerId);
        System.out.println("Received requiredSkills: " + requiredSkills);

        List<Application> applications = applicationRepository.findByOfferId(offerId);

        for (Application application : applications) {
            // Make sure the path is absolute and exists
            Path path = Paths.get("C:/Users/OCTANET/Documents/GitHub/CodingFactory/MicroservicePfe/uploads/cvs", application.getCV());
            File file = path.toFile();
            System.out.println("Processing CV file: " + file.getAbsolutePath());

            try {
                // Call Python script and get score
                float score = aiScoreService.calculateMatchScore(file, requiredSkills);
                System.out.println("Received score: " + score);
                application.setScore(score);
            } catch (IOException e) {
                System.out.println("IOException occurred: " + e.getMessage());
                application.setScore(0f);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid score format received from Python.");
                application.setScore(0f);
            }
        }

        return applications;

    }

    @GetMapping("/{offerId}/required-skills")
    public ResponseEntity<String> getRequiredSkillsForOffer(@PathVariable Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElse(null);

        if (offer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Offer not found with id " + offerId + "\"}");
        }

        // Assuming the offer contains a string of comma-separated skills
        String requiredSkills = offer.getRequiredSkill(); // e.g., "Java, Spring Boot, Docker, HTML"

        // Return the required skills as a JSON response
        return ResponseEntity.ok("{\"requiredSkills\": \"" + requiredSkills + "\"}");
    }


    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }
    @GetMapping("/company-representative/{userId}")
    public ResponseEntity<List<Application>> getApplicationsForCompanyRepresentative(@PathVariable Long userId) {
        List<Application> applications = applicationService.getApplicationsForCompanyRepresentative(userId);
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Application>> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PutMapping("/updateapp/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application application) {
        return ResponseEntity.ok(applicationService.updateApplication(id, application));
    }

    @DeleteMapping("/deleteapp/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/application/{id}/applicant-name")
    public ResponseEntity<String> getApplicantName(@PathVariable Long id) {
        String fullName = applicationService.getApplicantFullName(id);
        return ResponseEntity.ok(fullName);
    }
    @PostMapping("/upload-cv")
    public ResponseEntity<String> uploadCv(@RequestParam("cvFile") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }

        // Check if the file is a PDF
        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed.");
        }

        try {
            // Define the upload directory (in the project's root folder)
            String uploadDir = "uploads/cvs";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate a unique filename
            String originalFilename = file.getOriginalFilename();

            // Store the file
            Path filePath = uploadPath.resolve(originalFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return success message with the filename
            return ResponseEntity.ok("File uploaded successfully: " + originalFilename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
        }
    }
}