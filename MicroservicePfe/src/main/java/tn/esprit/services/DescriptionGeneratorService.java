package tn.esprit.services;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.esprit.entities.DescriptionRequestDTO;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

// DescriptionGeneratorService.java
@Service
public class DescriptionGeneratorService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String MODEL = "gpt-3.5-turbo";
    private static final double TEMPERATURE = 0.7;
    private static final int MAX_TOKENS = 500;

    public String generateDescription(DescriptionRequestDTO request) {
        try {
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));

            // Create message using setters
            ChatMessage message = new ChatMessage();
            message.setRole("user");
            message.setContent(buildPrompt(request));

            // Create request using setters
            ChatCompletionRequest completionRequest = new ChatCompletionRequest();
            completionRequest.setModel("gpt-3.5-turbo");
            completionRequest.setMessages(Collections.singletonList(message));
            completionRequest.setTemperature(0.7);
            completionRequest.setMaxTokens(500);

            return service.createChatCompletion(completionRequest)
                    .getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            throw new RuntimeException("Generation failed: " + e.getMessage());
        }
    }
    private String buildPrompt(DescriptionRequestDTO request) {
        return String.format(
                "Generate a professional job description based on these parameters:\n" +
                        "Job Title: %s\n" +
                        "Required Skills: %s\n" +
                        "Duration: %s\n" +
                        "Location: %s\n" +
                        "Employment Type: %s\n" +
                        "Responsibilities: %s\n\n" +
                        "The description should be between 20-50 words, professional tone, and include sections for:\n" +
                        "- Company introduction\n" +
                        "- Key responsibilities\n" +
                        "- Required qualifications\n" +
                        "- What we offer\n" +
                        "Avoid markdown formatting.",
                request.getTitle(),
                String.join(", ", request.getRequiredSkill()),
                request.getDuration(),
                request.getLocation(),
                request.getEmploymentType(),
                request.getJobResponsibilities()
        );
    }
}