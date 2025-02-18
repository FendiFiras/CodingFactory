package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Forum;
import tn.esprit.Services.IForumService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ForumController {

    private final IForumService forumService;

    @PostMapping("/AddForum/{userId}")
    public ResponseEntity<Forum> addForum(@RequestBody Forum forum, @PathVariable Long userId) {
        return ResponseEntity.ok(forumService.addForum(forum, userId));
    }

    @GetMapping("/GetAllForums")
    public ResponseEntity<List<Forum>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/GetForumBy/{id}")
    public ResponseEntity<Forum> getForumById(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.getOneById(id));
    }

    @PutMapping("/UpdateForum/{userId}")
    public ResponseEntity<Forum> updateForum(@RequestBody Forum forum) {
        return ResponseEntity.ok(forumService.updateForum(forum));
    }

    @DeleteMapping("/delete/{forumId}")
    public String deleteForum(@PathVariable Long forumId) {
        try {
            // Appeler la méthode du service pour supprimer le forum
            forumService.deleteForum(forumId);
            return "Forum deleted successfully.";
        } catch (IllegalArgumentException e) {
            return "Forum not found with ID: " + forumId;
        }
    }

}
