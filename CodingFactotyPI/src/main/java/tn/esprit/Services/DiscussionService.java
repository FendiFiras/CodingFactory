package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Discussion;
import tn.esprit.entities.User;
import tn.esprit.Repository.DiscussionRepository;
import tn.esprit.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DiscussionService implements IDiscussionService {

    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;

    @Override
    public Discussion addDiscussion(Discussion discussion, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            discussion.setPublicationDate(new java.util.Date());
            discussion = discussionRepository.save(discussion); // Sauvegarder la discussion d'abord pour lui donner un ID valide

            user.getDiscussions().add(discussion); // Ajouter la discussion à la liste des discussions de l'utilisateur
            userRepository.save(user); // Sauvegarder l'utilisateur pour mettre à jour la table d'association

            return discussion;
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

    @Override
    public void deleteDiscussion(Long discussionId) {
        // Vérifier si la discussion existe
        if (!discussionRepository.existsById(discussionId)) {
            throw new IllegalArgumentException("Discussion not found with ID: " + discussionId);
        }

        // Récupérer la discussion
        Discussion discussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new IllegalArgumentException("Discussion not found with ID: " + discussionId));

        // Dissocier les utilisateurs de cette discussion
        List<User> usersInDiscussion = userRepository.findUsersByDiscussionId(discussionId); // Méthode custom pour récupérer les utilisateurs associés à la discussion
        for (User user : usersInDiscussion) {
            user.getDiscussions().remove(discussion); // Retirer cette discussion de l'utilisateur
            userRepository.save(user); // Sauvegarder les changements dans l'utilisateur
        }

        // Supprimer la discussion
        discussionRepository.deleteById(discussionId);
    }

    @Override
    public Discussion updateDiscussion(Discussion discussion) {
        Optional<Discussion> existingDiscussionOpt = discussionRepository.findById(discussion.getDiscussion_id());
        if (existingDiscussionOpt.isPresent()) {
            Discussion existingDiscussion = existingDiscussionOpt.get();
            existingDiscussion.setTitle(discussion.getTitle());
            existingDiscussion.setDescription(discussion.getDescription());
            return discussionRepository.save(existingDiscussion);
        } else {
            throw new IllegalArgumentException("Discussion not found with ID: " + discussion.getDiscussion_id());
        }
    }

    @Override
    public Discussion getOneById(Long id) {
        return discussionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discussion not found with ID: " + id));
    }

    @Override
    public List<Discussion> getAllDiscussions() {
        return discussionRepository.findAll();
    }
}
