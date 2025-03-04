package tn.esprit.Services;

import tn.esprit.entities.Like;

import java.util.List;
import java.util.Map;


public interface ILikeService {
    Like addLike(Like like, Long userId, Long messageId);
    Like getLike(Long id);
    List<Map<String, Object>> getAllLikes();  // Type de retour doit correspondre
    void deleteLike(Long id);
    Like updateLike(Long id, Like like);
}