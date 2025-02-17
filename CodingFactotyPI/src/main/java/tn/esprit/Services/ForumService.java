package tn.esprit.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.UserRepository;
import tn.esprit.entities.Forum;
import tn.esprit.Repository.ForumRepository;
import tn.esprit.entities.User;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService implements IForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }

    @Override
    public Forum getForumById(Long id) {
        return forumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum not found with id: " + id));
    }



    @Override
    public Forum createForum(Long userId, Forum forum) {
        // Utilisation correcte de findById()
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        forum = forumRepository.save(forum); // Sauvegarde du forum

        user.getForums().add(forum); // Associe le forum à l'utilisateur
        userRepository.save(user); // Met à jour l'association dans user_forums

        return forum;
    }

    @Override
    public Forum updateForum(Long id, Forum forum) {
        Optional<Forum> existingForum = forumRepository.findById(id);
        if (existingForum.isPresent()) {
            Forum updatedForum = existingForum.get();
            updatedForum.setTitle(forum.getTitle());
            updatedForum.setDescription(forum.getDescription());
            updatedForum.setImage(forum.getImage());
            return forumRepository.save(updatedForum);
        } else {
            throw new RuntimeException("Forum not found with id: " + id);
        }
    }

    @Override
    public void deleteForum(Long id) {
        if (forumRepository.existsById(id)) {
            forumRepository.deleteById(id);
        } else {
            throw new RuntimeException("Forum not found with id: " + id);
        }
    }
}