package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.ForumRepository;
import tn.esprit.entities.Discussion;
import tn.esprit.Repository.DiscussionRepository;
import tn.esprit.entities.Forum;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscussionService implements IDiscussionService {

    private final DiscussionRepository discussionRepository;
    private final ForumRepository forumRepository;

    @Override
    public Discussion addDiscussion(Discussion discussion, Long forumId) {
        // Récupérer le forum
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Forum avec ID " + forumId + " non trouvé !"));

        // Initialiser les valeurs de la discussion
        discussion.setNumberOfLikes(0L);
        discussion.setPublicationDate(new Date());

        // Ajouter la discussion à la liste des discussions du forum
        forum.getDiscussions().add(discussion);

        // Sauvegarder le forum (cela sauvegarde aussi la discussion grâce à la relation bidirectionnelle)
        forumRepository.save(forum);

        return discussion;
    }
}
