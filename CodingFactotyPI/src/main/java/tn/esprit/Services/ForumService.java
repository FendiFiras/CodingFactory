package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Forum;
import tn.esprit.entities.User;
import tn.esprit.Repository.ForumRepository;
import tn.esprit.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ForumService implements IForumService {

    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    @Override
    public Forum saveForum(Forum forum) {
        return forumRepository.save(forum);  // Save the forum to the database
    }

    @Override
    public Forum addForum(Forum forum, Long idUser) {
        Optional<User> userOpt = userRepository.findById(idUser);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            forum.setCreationDate(new java.util.Date());
            forum = forumRepository.save(forum); // Save the forum first to give it a valid ID

            user.getForums().add(forum); // Add the forum to the user's list of forums
            userRepository.save(user);   // Save the user to update the association table

            return forum;
        } else {
            throw new IllegalArgumentException("User not found with ID: " + idUser);
        }
    }

    @Override
    public void deleteForum(Long forumId) {
        // Check if the forum exists
        if (!forumRepository.existsById(forumId)) {
            throw new IllegalArgumentException("Forum not found with ID: " + forumId);
        }

        // Retrieve the forum
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new IllegalArgumentException("Forum not found with ID: " + forumId));

        // Dissociate users from this forum
        List<User> usersInForum = userRepository.findUsersByForumId(forumId);  // Use custom method to retrieve users associated with the forum
        for (User user : usersInForum) {
            user.getForums().remove(forum);  // Remove this forum from the user
            userRepository.save(user);  // Save changes to the user
        }

        // Delete the forum
        forumRepository.deleteById(forumId);
    }

    @Override
    public Forum updateForum(Forum forum) {
        Optional<Forum> existingForumOpt = forumRepository.findById(forum.getForum_id());
        if (existingForumOpt.isPresent()) {
            Forum existingForum = existingForumOpt.get();
            existingForum.setTitle(forum.getTitle());
            existingForum.setDescription(forum.getDescription());
            existingForum.setImage(forum.getImage());
            return forumRepository.save(existingForum);
        } else {
            throw new IllegalArgumentException("Forum not found with ID: " + forum.getForum_id());
        }
    }

    @Override
    public Forum getOneById(Long id) {
        return forumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Forum not found with ID: " + id));
    }

    @Override
    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }
}