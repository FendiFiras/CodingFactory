package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Discussion;
import tn.esprit.Services.IDiscussionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200") // Allow CORS for all endpoints

@RestController
@RequiredArgsConstructor
public class DiscussionController {

    private final IDiscussionService discussionService;

    @PostMapping("/add/{userId}/{forumId}")
    public ResponseEntity<Discussion> addDiscussionToForum(
            @RequestBody Discussion discussion,
            @PathVariable Long userId,
            @PathVariable Long forumId) {

        Discussion newDiscussion = discussionService.addDiscussion(discussion, userId, forumId);
        return ResponseEntity.ok(newDiscussion);
    }


    @GetMapping("/GetAllDiscussions")
    public ResponseEntity<List<Discussion>> getAllDiscussions() {
        return ResponseEntity.ok(discussionService.getAllDiscussions());
    }
    @GetMapping("/forum/{forumId}")
    public ResponseEntity<List<Discussion>> getDiscussionsByForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(discussionService.getDiscussionsByForum(forumId));
    }
    @GetMapping("/GetDiscussionBy/{id}")
    public ResponseEntity<Discussion> getDiscussionById(@PathVariable Long id) {
        return ResponseEntity.ok(discussionService.getOneById(id));
    }

    @PutMapping("/UpdateDiscussion/{discussionId}")
    public ResponseEntity<Discussion> updateDiscussion(@RequestBody Discussion discussion) {
        return ResponseEntity.ok(discussionService.updateDiscussion(discussion));
    }



    @DeleteMapping("/deleteDiscussion/{discussionId}")
    public ResponseEntity<Map<String, String>> deleteDiscussion(@PathVariable Long discussionId) {
        try {
            // Supprimer la discussion
            discussionService.deleteDiscussion(discussionId);

            // Retourner une réponse JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Discussion deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Retourner une erreur JSON si la discussion n'existe pas
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }




}