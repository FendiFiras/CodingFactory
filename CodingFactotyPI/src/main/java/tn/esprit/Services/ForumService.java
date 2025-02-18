package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Forum;
import tn.esprit.entities.User;
import tn.esprit.Repository.ForumRepository;
import tn.esprit.Repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ForumService implements IForumService {

    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    @Override
    public Forum addForum(Forum forum, Long idUser) {
        Optional<User> userOpt = userRepository.findById(idUser);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            forum.setCreationDate(new java.util.Date());
            forum = forumRepository.save(forum); // Sauvegarder le forum d'abord pour lui donner un ID valide

            user.getForums().add(forum); // Ajouter le forum à la liste des forums de l'utilisateur
            userRepository.save(user);   // Sauvegarder l'utilisateur pour mettre à jour la table d'association

            return forum;
        } else {
            throw new IllegalArgumentException("User not found with ID: " + idUser);
        }
    }


    @Override
    public void deleteForum(Long forumId) {
        // Vérifier si le forum existe
        if (!forumRepository.existsById(forumId)) {
            throw new IllegalArgumentException("Forum not found with ID: " + forumId);
        }

        // Récupérer le forum
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new IllegalArgumentException("Forum not found with ID: " + forumId));

        // Dissocier les utilisateurs de ce forum
        List<User> usersInForum = userRepository.findUsersByForumId(forumId);  // Utilisation de la méthode custom pour récupérer les utilisateurs associés au forum
        for (User user : usersInForum) {
            user.getForums().remove(forum);  // Retirer ce forum de l'utilisateur
            userRepository.save(user);  // Sauvegarder les changements dans l'utilisateur
        }

        // Supprimer le forum
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
