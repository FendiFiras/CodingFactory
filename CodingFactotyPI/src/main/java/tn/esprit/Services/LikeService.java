package tn.esprit.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.Services.ILikeService;
import tn.esprit.entities.Like;
import tn.esprit.entities.User;
import tn.esprit.entities.Message;
import tn.esprit.Repository.LikeRepository;
import tn.esprit.Repository.UserRepository;
import tn.esprit.Repository.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class LikeService implements ILikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Like addLike(Like like, Long userId, Long messageId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Message> messageOptional = messageRepository.findById(messageId);

        if (userOptional.isPresent() && messageOptional.isPresent()) {
            like.setUser(userOptional.get());
            like.setMessage(messageOptional.get());
            return likeRepository.save(like);
        }
        return null;
    }

    @Override
    public Like getLike(Long id) {
        Optional<Like> like = likeRepository.findById(id);
        return like.orElse(null);
    }

    @Override
    public List<Map<String, Object>> getAllLikes() {
        List<Like> likes = likeRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Like like : likes) {
            Map<String, Object> likeData = new HashMap<>();
            likeData.put("likeId", like.getLike_id());
            likeData.put("userId", like.getUser() != null ? like.getUser().getIdUser() : null);
            likeData.put("messageId", like.getMessage() != null ? like.getMessage().getMessage_id() : null);
            likeData.put("likedeslike", like.isLikedeslike());

            result.add(likeData);
        }

        return result;
    }

    @Override
    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }

    @Override
    public Like updateLike(Long id, Like like) {
        if (likeRepository.existsById(id)) {
            like.setLike_id(id);
            return likeRepository.save(like);
        }
        return null;
    }
}