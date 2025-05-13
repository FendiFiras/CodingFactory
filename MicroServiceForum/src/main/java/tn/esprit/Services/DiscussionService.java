package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.ForumRepository;
import tn.esprit.entities.Discussion;
import tn.esprit.entities.Forum;
import tn.esprit.entities.User;
import tn.esprit.Repository.DiscussionRepository;
import tn.esprit.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DiscussionService implements IDiscussionService {

    private final DiscussionRepository discussionRepository;
    private final ForumRepository forumRepository;

    private final UserRepository userRepository;


    public List<Discussion> getDiscussionsByForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Forum not found"));
        return forum.getDiscussions(); // Fetch discussions directly from the Forum entity
    }
    @Override
    public Discussion addDiscussion(Discussion discussion, Long userId, Long forumId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Forum> forumOpt = forumRepository.findById(forumId);

        if (userOpt.isPresent() && forumOpt.isPresent()) {
            User user = userOpt.get();
            Forum forum = forumOpt.get();

            discussion.setPublicationDate(new java.util.Date());
            discussion = discussionRepository.save(discussion); // Sauvegarder la discussion pour lui donner un ID valide

            // Ajouter la discussion au forum
            forum.getDiscussions().add(discussion);
            forumRepository.save(forum); // Sauvegarder le forum pour mettre à jour la relation

            // Ajouter la discussion à l'utilisateur
            user.getDiscussions().add(discussion);
            userRepository.save(user); // Sauvegarder l'utilisateur pour mettre à jour la relation


            return discussion;
        } else {
            throw new IllegalArgumentException("User or Forum not found with provided IDs");
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

        // Dissocier la discussion des forums
        List<Forum> forumsContainingDiscussion = forumRepository.findForumsByDiscussionId(discussionId); // Méthode custom pour récupérer les forums associés à la discussion
        for (Forum forum : forumsContainingDiscussion) {
            forum.getDiscussions().remove(discussion); // Retirer la discussion du forum
            forumRepository.save(forum); // Sauvegarder les changements dans le forum
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