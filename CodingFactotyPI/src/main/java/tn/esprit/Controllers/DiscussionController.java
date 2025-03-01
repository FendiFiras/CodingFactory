package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Discussion;
import tn.esprit.Services.IDiscussionService;

import java.util.List;
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
    public String deleteDiscussion(@PathVariable Long discussionId) {
        try {
            discussionService.deleteDiscussion(discussionId);
            return "Discussion deleted successfully.";
        } catch (IllegalArgumentException e) {
            return "Discussion not found with ID: " + discussionId;
        }
    }




}