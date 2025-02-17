package tn.esprit.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Forum;
import tn.esprit.Services.IForumService;

import java.util.List;

@RestController
@RequestMapping("/forum")
@CrossOrigin("*") // Permet d'éviter les problèmes CORS
public class ForumController {

    @Autowired
    private IForumService forumService;

    @GetMapping("/all")
    public ResponseEntity<List<Forum>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Forum> getForumById(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.getForumById(id));
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<Forum> createForum(@PathVariable Long userId, @RequestBody Forum forum) {
        return ResponseEntity.ok(forumService.createForum(userId, forum));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Forum> updateForum(@PathVariable Long id, @RequestBody Forum forum) {
        return ResponseEntity.ok(forumService.updateForum(id, forum));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteForum(@PathVariable Long id) {
        forumService.deleteForum(id);
        return ResponseEntity.ok("Forum deleted successfully");
    }
}