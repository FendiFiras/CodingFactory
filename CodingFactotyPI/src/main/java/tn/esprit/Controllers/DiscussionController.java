package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Discussion;
import tn.esprit.Services.IDiscussionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiscussionController {

    private final IDiscussionService discussionService;

    @PostMapping("/AddDiscussion/{userId}")
    public ResponseEntity<Discussion> addDiscussion(@RequestBody Discussion discussion, @PathVariable Long userId) {
        return ResponseEntity.ok(discussionService.addDiscussion(discussion, userId));
    }

    @GetMapping("/GetAllDiscussions")
    public ResponseEntity<List<Discussion>> getAllDiscussions() {
        return ResponseEntity.ok(discussionService.getAllDiscussions());
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
