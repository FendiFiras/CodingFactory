package tn.esprit.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Like;
import tn.esprit.Services.ILikeService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final ILikeService likeService;

    @Autowired
    public LikeController(ILikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/add/{userId}/{messageId}")
    public Like addLike(@RequestBody Like like, @PathVariable Long userId, @PathVariable Long messageId) {
        return likeService.addLike(like, userId, messageId);
    }

    @GetMapping("/{id}")
    public Like getLike(@PathVariable Long id) {
        return likeService.getLike(id);
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllLikes() {
        return likeService.getAllLikes();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
    }

    @PutMapping("/update/{id}")
    public Like updateLike(@PathVariable Long id, @RequestBody Like like) {
        return likeService.updateLike(id, like);
    }
}