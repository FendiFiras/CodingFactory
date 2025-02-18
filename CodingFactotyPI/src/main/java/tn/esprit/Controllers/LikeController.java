/*
package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.ILikeService;
import tn.esprit.entities.Like;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final ILikeService likeService;

    @PostMapping("/{userId}/{messageId}")
    public ResponseEntity<Like> likeOrDislike(@PathVariable Long userId, @PathVariable Long messageId, @RequestParam boolean liked) {
        Like like = likeService.likeOrDislikeMessage(userId, messageId, liked);
        return ResponseEntity.ok(like);
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<String> deleteLike(@PathVariable Long likeId) {
        likeService.deleteLike(likeId);
        return ResponseEntity.ok("Like supprimé avec succès");
    }
}


 */