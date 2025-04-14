package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Chemin complet du fichier
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        // Créer le dossier s'il n'existe pas
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        // Enregistrer le fichier
        Files.copy(file.getInputStream(), filePath);

        // Retourner l'URL du fichier
        return  fileName;
    }
}