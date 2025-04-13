package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

@Service
public class CheatDetectionService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean detectCheating(Map<String, Object> payload) {
        String url = "http://127.0.0.1:5000/predict";

        // ✅ Tous les 7 paramètres nécessaires au modèle Python
        Map<String, Object> features = new HashMap<>();
        features.put("duration", payload.get("duration"));
        features.put("clicks", payload.get("clicks"));
        features.put("fast_answers", payload.get("fast_answers"));
        features.put("tab_switches", payload.get("tab_switches"));
        features.put("idle_time", payload.get("idle_time"));
        features.put("wrong_answers", payload.get("wrong_answers"));
        features.put("head_turns", payload.get("head_turns")); // 👈 important

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(features, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        // ✅ Attention au bon nom du champ retourné : "cheating"
        return (boolean) response.getBody().get("cheating");
    }
}
