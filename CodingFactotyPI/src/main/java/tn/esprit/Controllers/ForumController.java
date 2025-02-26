package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Repository.ForumRepository;
import tn.esprit.entities.Forum;
import tn.esprit.Services.IForumService;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Allow CORS for all endpoints
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forum")
public class ForumController {


    private final IForumService forumService;
    private final ForumRepository forumRepository;

    @PostMapping("/AddForum/{userId}")
    public ResponseEntity<Forum> addForum(
            @PathVariable Long userId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        System.out.println("UserID: " + userId);
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Image: " + (image != null ? image.getOriginalFilename() : "No image"));

        if (title == null || description == null) {
            return ResponseEntity.badRequest().body(null);
        }

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            imagePath = saveImage(image);
        }

        Forum forum = new Forum();
        forum.setTitle(title);
        forum.setDescription(description);
        forum.setImage(imagePath);
        forum.setCreationDate(new Date());

        Forum savedForum = forumService.addForum(forum, userId);
        return ResponseEntity.ok(savedForum);
    }


    private String saveImage(MultipartFile image) {
        // Directory where images will be stored
        String uploadDir = "C:/uploads/";

        // Create the directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();  // Create the directory if it doesn't exist
        }

        // Create a unique name for the image
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename().replace(" ", "_");
        String imagePath = uploadDir + fileName;

        // Save the image to the directory
        try {
            File file = new File(imagePath);
            image.transferTo(file);  // Save the image to disk
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving the image.");
        }

        return fileName;  // Return the filename to save in the database
    }

    @GetMapping("/GetAllForums")
    public ResponseEntity<List<Forum>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/GetForumBy/{id}")
    public ResponseEntity<Forum> getForumById(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.getOneById(id));
    }

    @PutMapping("/UpdateForum/{forumId}")
    public ResponseEntity<Forum> updateForum(
            @PathVariable Long forumId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Forum forum = forumRepository.findById(forumId).orElse(null);
        if (forum == null) {
            return ResponseEntity.notFound().build();  // Forum non trouvé
        }

        // Si le titre est fourni, mettre à jour le titre
        if (title != null) {
            forum.setTitle(title);
        }

        // Si la description est fournie, mettre à jour la description
        if (description != null) {
            forum.setDescription(description);
        }

        // Si une image est fournie, la mettre à jour
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);  // Méthode de sauvegarde de l'image
            forum.setImage(imagePath);
        }

        // Mettre à jour la date de modification (optionnel)
        forum.setCreationDate(new Date());  // Vous pouvez aussi stocker une `modificationDate` séparée si nécessaire

        Forum updatedForum = forumService.updateForum(forum);
        return ResponseEntity.ok(updatedForum);
    }

    @DeleteMapping("/delete/{forum_id}")
    public ResponseEntity<String> deleteForum(@PathVariable Long forum_id) {
        if (!forumRepository.existsById(forum_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Forum not found");
        }

        forumService.deleteForum(forum_id);
        return ResponseEntity.ok("Forum deleted successfully");
    }
}