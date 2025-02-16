package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Discussion;
import tn.esprit.Services.IDiscussionService;

@RestController
@RequestMapping("/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final IDiscussionService discussionService;

    @PostMapping("/add/{forumId}")
    public ResponseEntity<Discussion> addDiscussion(@RequestBody Discussion discussion, @PathVariable Long forumId) {
        Discussion newDiscussion = discussionService.addDiscussion(discussion, forumId);
        return ResponseEntity.ok(newDiscussion);
    }
}
