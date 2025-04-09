package tn.esprit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.services.AiScoreService;

@RestController
@RequestMapping("/api/score")
public class AiScoreController {

    @Autowired
    private AiScoreService aiScoreService;

    @PostMapping("/calculate")
    public ResponseEntity<Float> calculateMatchScore(
            @RequestParam("file") MultipartFile file,
            @RequestParam("requiredSkills") String requiredSkills) {

        try {
            // Call AI score calculation method
            float matchScore = aiScoreService.calculateMatchScore(file, requiredSkills);
            return new ResponseEntity<>(matchScore, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
