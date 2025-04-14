package tn.esprit.controllers;

import com.theokanning.openai.OpenAiHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.DescriptionRequestDTO;
import tn.esprit.services.DescriptionGeneratorService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// DescriptionGeneratorController.java
@RestController
@RequestMapping("/generate-description")
@CrossOrigin(origins = "http://localhost:4200")
public class DescriptionGeneratorController {

    private final DescriptionGeneratorService descriptionService;
    private static final Logger log = LoggerFactory.getLogger(DescriptionGeneratorService.class);

    public DescriptionGeneratorController(DescriptionGeneratorService descriptionService) {
        this.descriptionService = descriptionService;
    }

    @PostMapping
    public ResponseEntity<?> generateDescription(
            @RequestBody DescriptionRequestDTO request) {
        try {
            String generatedText = descriptionService.generateDescription(request);
            return ResponseEntity.ok(Collections.singletonMap("generatedText", generatedText));
        } catch (OpenAiHttpException e) {
            log.error("OpenAI API error: {}", e.getMessage());
            return ResponseEntity.status(e.statusCode)
                    .body(Collections.singletonMap("error", "AI service error: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Generation failed: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "Generation failed: " + e.getMessage()));
        }
    }
}

// DescriptionRequestDTO.java

