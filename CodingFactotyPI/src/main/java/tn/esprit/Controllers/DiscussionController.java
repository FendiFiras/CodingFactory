package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Discussion;
import tn.esprit.Services.IDiscussionService;
import java.util.List;

@RestController
@RequestMapping("/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final IDiscussionService discussionService;

    @PostMapping("/add/{forumId}/{idUser}")
    public ResponseEntity<Discussion> addDiscussion(@RequestBody Discussion discussion, @PathVariable Long forumId , @PathVariable Long idUser) {
        Discussion newDiscussion = discussionService.addDiscussion(discussion, forumId, idUser);
        return ResponseEntity.ok(newDiscussion);
    }
    @GetMapping("/{discussionId}")
    public ResponseEntity<Discussion> getDiscussionById(@PathVariable Long discussionId) {
        return ResponseEntity.ok(discussionService.getDiscussionById(discussionId));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Discussion>> getAllDiscussions() {
        return ResponseEntity.ok(discussionService.getAllDiscussions());
    }

    @PutMapping("/update/{discussionId}")
    public ResponseEntity<Discussion> updateDiscussion(@PathVariable Long discussionId, @RequestBody Discussion discussion) {
        return ResponseEntity.ok(discussionService.updateDiscussion(discussionId, discussion));
    }

    @DeleteMapping("/delete/{discussionId}")
    public ResponseEntity<Void> deleteDiscussion(@PathVariable Long discussionId) {
        discussionService.deleteDiscussion(discussionId);
        return ResponseEntity.noContent().build();
    }






}
