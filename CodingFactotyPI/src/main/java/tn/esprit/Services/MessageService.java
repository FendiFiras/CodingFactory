package tn.esprit.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.Message;
import tn.esprit.entities.User;
import tn.esprit.entities.Discussion;

import java.util.Date;
import java.util.Optional;
import java.util.List;

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



    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        // Désaffecter le message de la discussion
        Discussion discussion = discussionRepository.findByMessagesContaining(message);
        if (discussion != null) {
            discussion.getMessages().remove(message);
            discussionRepository.save(discussion);
        }

        // Désaffecter le message du user
        User user = userRepository.findByMessagesContaining(message);
        if (user != null) {
            user.getMessages().remove(message);
            userRepository.save(user);
        }

        // Supprimer le message de la base de données
        messageRepository.delete(message);
    }

    public Message getMessageById(Long message_id) {
        return messageRepository.findById(message_id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

}
