/*
package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.LikeRepository;
import tn.esprit.Repository.MessageRepository;
import tn.esprit.Repository.UserRepository;
import tn.esprit.entities.Like;
import tn.esprit.entities.Message;
import tn.esprit.entities.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService implements ILikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public Like likeOrDislikeMessage(Long userId, Long messageId, boolean liked) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec ID " + userId + " non trouvé !"));

        // Vérifier si le message existe
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message avec ID " + messageId + " non trouvé !"));

        // Vérifier si l'utilisateur a déjà liké/disliké ce message
        Optional<Like> existingLike = likeRepository.findByUserAndMessage(user, message);

        if (existingLike.isPresent()) {
            Like like = existingLike.get();
            // Si l'utilisateur clique à nouveau sur le même bouton, on supprime le like/dislike
            if (like.isLikedeslike() == liked) {
                likeRepository.delete(like);
                return null; // Like supprimé
            } else {
                // Sinon, on change le statut (like → dislike ou dislike → like)
                like.setLikedeslike(liked);
                return likeRepository.save(like);
            }
        } else {
            // Créer un nouveau like/dislike
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setMessage(message);
            newLike.setLikedeslike(liked);
            return likeRepository.save(newLike);
        }
    }

    @Override
    public void deleteLike(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}


 */