package tn.esprit.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.entities.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiScoreService {

    public float calculateMatchScore(File file, String requiredSkills) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "Ai/ai_score.py", file.getAbsolutePath(), requiredSkills);
        processBuilder.redirectErrorStream(true); // Merges stderr with stdout

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String lastLine = null;

        while ((line = reader.readLine()) != null) {
            lastLine = line;  // Keeps overwriting until the last line
        }

        try {
            return Float.parseFloat(lastLine.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid score format received from Python: " + lastLine);
            return 0f;
        }
    }







}

