package tn.esprit.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AiScoreService {

    public float calculateMatchScore(MultipartFile file, String requiredSkills) throws IOException {
        // Save the file to the server (in this case, in a folder called "uploads/cvs")
        Path path = Paths.get("uploads/cvs/" + file.getOriginalFilename());
        File destinationFile = path.toFile();
        file.transferTo(destinationFile);

        // Now call the Python script with the file path and required skills as arguments
        String command = "python ai_score.py \"" + destinationFile.getAbsolutePath() + "\" \"" + requiredSkills + "\"";

        // Execute the Python script and capture the output
        Process process = Runtime.getRuntime().exec(command);
        StringBuilder output = new StringBuilder();

        // Capture the standard output of the process (i.e., the score)
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // Wait for the process to finish and check the exit code
        try {
            int exitCode = process.waitFor();
            System.out.println("Process completed with exit code: " + exitCode);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore the interrupted status
            System.out.println("Thread was interrupted while waiting for the process.");
        }

        // Parse the output to get the match score (assumed to be a float)
        // You can adjust this as per your Python script's output format
        String scoreString = output.toString().trim();  // Clean up any extra newlines or spaces
        float score = 0.0f;
        try {
            score = Float.parseFloat(scoreString);
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse the match score: " + scoreString);
        }

        return score;
    }
}
