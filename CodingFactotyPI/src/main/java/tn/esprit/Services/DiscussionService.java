/*
package tn.esprit.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.ForumRepository;
import tn.esprit.entities.Discussion;
import tn.esprit.Repository.DiscussionRepository;
import tn.esprit.entities.Forum;
import tn.esprit.entities.User;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor

public class DiscussionService implements IDiscussionService {

    private final DiscussionRepository discussionRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository; // Ajout du repository U

    @Override
    public Discussion addDiscussion(Discussion discussion, Long forumId,Long idUser) {
        // Récupérer le forum
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Forum avec ID " + forumId + " non trouvé !"));

        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec ID " + idUser + " non trouvé !"));

        // Initialiser les valeurs de la discussion
        discussion.setNumberOfLikes(0L);
        discussion.setPublicationDate(new Date());

        // Ajouter la discussion à la liste des discussions du forum
        forum.getDiscussions().add(discussion);

        // Sauvegarder le forum (cela sauvegarde aussi la discussion grâce à la relation bidirectionnelle)
        forumRepository.save(forum);

        // Ajouter la discussion à la liste des discussions du User
        user.getDiscussions().add(discussion);
        userRepository.save(user); // Sauvegarder l'utilisateur


        return discussion;
    }



    @Override
    public Discussion getDiscussionById(Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new RuntimeException("Discussion avec ID " + discussionId + " non trouvée !"));
    }

    @Override
    public List<Discussion> getAllDiscussions() {
        return discussionRepository.findAll();
    }

    @Override
    public Discussion updateDiscussion(Long discussionId, Discussion updatedDiscussion) {
        Discussion existingDiscussion = getDiscussionById(discussionId);

        existingDiscussion.setTitle(updatedDiscussion.getTitle());
        existingDiscussion.setDescription(updatedDiscussion.getDescription());
        existingDiscussion.setNumberOfLikes(updatedDiscussion.getNumberOfLikes());

        return discussionRepository.save(existingDiscussion);
    }

    @Override
    @Transactional
    public void deleteDiscussion(Long discussionId) {
        Discussion discussion = getDiscussionById(discussionId);

        // Récupérer tous les forums et retirer la discussion
        List<Forum> forums = forumRepository.findAll();
        for (Forum forum : forums) {
            if (forum.getDiscussions().remove(discussion)) {
                forumRepository.save(forum); // Mettre à jour le forum
                break; // La discussion n'existe que dans un forum, inutile de continuer
            }
        }

        discussionRepository.delete(discussion);
    }






}

 */