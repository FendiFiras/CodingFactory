package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Message;
import tn.esprit.entities.User;
import tn.esprit.entities.Discussion;
import tn.esprit.Repository.MessageRepository;
import tn.esprit.Repository.UserRepository;
import tn.esprit.Repository.DiscussionRepository;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;

    public Message addMessageToDiscussionAndUser(Message message, Long userId, Long discussionId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Discussion> discussionOpt = discussionRepository.findById(discussionId);

        if (userOpt.isPresent() && discussionOpt.isPresent()) {
            User user = userOpt.get();
            Discussion discussion = discussionOpt.get();

            // Assigner la date de création du message
            message.setMessageDate(new Date());

            // Sauvegarder le message dans la table Message
            message = messageRepository.save(message);

            // Ajouter le message à l'utilisateur et à la discussion via les tables d'association
            // Ajout dans user_message (relation entre user et message)
            user.getMessages().add(message);
            userRepository.save(user);

            // Ajout dans discussion_message (relation entre discussion et message)
            discussion.getMessages().add(message);
            discussionRepository.save(discussion);

            return message;
        } else {
            throw new IllegalArgumentException("User or Discussion not found");
        }
    }

  /*  @Override
    public void deleteMessage(Long messageId) {
        // Vérifier si le message existe
        if (!messageRepository.existsById(messageId)) {
            throw new IllegalArgumentException("Message not found with ID: " + messageId);
        }

        // Récupérer le message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with ID: " + messageId));

        // Dissocier le message de l'utilisateur et de la discussion via les tables d'association
        User user = message.getUser();
        if (user != null) {
            user.getMessages().remove(message); // Retirer le message de l'utilisateur
            userRepository.save(user); // Sauvegarder les changements dans l'utilisateur
        }

        Discussion discussion = message.getDiscussion();
        if (discussion != null) {
            discussion.getMessages().remove(message); // Retirer le message de la discussion
            discussionRepository.save(discussion); // Sauvegarder les changements dans la discussion
        }

        // Supprimer le message
        messageRepository.deleteById(messageId);
    }

   */


    public Message updateMessage(Message updatedMessage) {
        Optional<Message> existingMessageOpt = messageRepository.findById(updatedMessage.getMessage_id());
        if (existingMessageOpt.isPresent()) {
            Message existingMessage = existingMessageOpt.get();
            existingMessage.setDescription(updatedMessage.getDescription());
            existingMessage.setImage(updatedMessage.getImage());
            existingMessage.setNumberOfLikes(updatedMessage.getNumberOfLikes());

            return messageRepository.save(existingMessage);
        } else {
            throw new IllegalArgumentException("Message not found");
        }
    }
}
