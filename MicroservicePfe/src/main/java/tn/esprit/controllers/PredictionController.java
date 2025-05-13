package tn.esprit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tn.esprit.entities.PredictionRequest;

import java.util.Map;

@RestController
public class PredictionController {
    @CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular app
    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestBody PredictionRequest input) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/predict";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, input, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur IA");
        }
    }

}
