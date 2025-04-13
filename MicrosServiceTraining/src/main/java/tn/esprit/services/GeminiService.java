package tn.esprit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateQuizQuestions(String topic, int numberOfQuestions) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ Créer le prompt
        String prompt = "Generate " + numberOfQuestions + " multiple-choice quiz questions about " + topic +
                ". Each question must have 4 options (a, b, c, d) and indicate the correct answer clearly like: 'Answer: b'";

        // ✅ Corps de la requête selon l’API Gemini
        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(textPart));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String rawJson = response.getBody();

                // ✅ Parser JSON et extraire uniquement le texte généré
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(rawJson);

                JsonNode textNode = root
                        .path("candidates").get(0)
                        .path("content")
                        .path("parts").get(0)
                        .path("text");

                if (textNode.isMissingNode()) {
                    System.out.println("❌ Aucune partie 'text' trouvée dans la réponse Gemini !");
                    return "";
                }

                String cleanText = textNode.asText();
                System.out.println("✅ Contenu généré par Gemini : \n" + cleanText);
                return cleanText;

            } else {
                throw new RuntimeException("❌ Erreur Gemini API : " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("❌ Exception lors de l’appel Gemini : " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}