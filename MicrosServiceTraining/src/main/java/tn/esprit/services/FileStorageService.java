package tn.esprit.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "C:/uploads/";

    public List<String> saveFiles(List<MultipartFile> files) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        // 📂 Création du dossier si nécessaire
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // 🔥 Générer un nom de fichier unique
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

                // 📂 Sauvegarde du fichier
                Files.copy(file.getInputStream(), filePath);

                // ✅ Ajouter l'URL dans la liste
                fileUrls.add("http://localhost:8089/Courses/" + fileName);
            }
        }
        return fileUrls;
    }






















}
