/*
package tn.esprit.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.DiscussionRepository;
import tn.esprit.Repository.MessageRepository;
import tn.esprit.entities.Discussion;
import tn.esprit.entities.Message;
import tn.esprit.entities.User;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;

    @Override
    @Transactional
    public Message addMessage(Message message, Long userId, Long discussionId) {
        // Récupérer l'utilisateur et la discussion
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable !"));

        Discussion discussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new RuntimeException("Discussion introuvable !"));

        // Initialiser le message
        message.setMessageDate(new Date());
        message.setNumberOfLikes(0L);

        // Sauvegarder d'abord le message
        Message savedMessage = messageRepository.save(message);

        // Ajouter le message à la liste des messages de l'utilisateur et de la discussion
        user.getMessages().add(savedMessage);
        discussion.getMessages().add(savedMessage);

        // Sauvegarder les relations
        userRepository.save(user);
        discussionRepository.save(discussion);

        return savedMessage;
    }



    @Override
    public Message getMessageById(Long message_id) {
        return messageRepository.findById(message_id)
                .orElseThrow(() -> new RuntimeException("Message avec ID " + message_id + " introuvable !"));
    }





    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessage(Long messageId, Message updatedMessage) {
        Message existingMessage = getMessageById(messageId);

        existingMessage.setDescription(updatedMessage.getDescription());
        existingMessage.setImage(updatedMessage.getImage());

        return messageRepository.save(existingMessage);
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = getMessageById(messageId);

        // Retirer le message des listes associées
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getMessages().remove(message)) {
                userRepository.save(user);
                break;
            }
        }

        List<Discussion> discussions = discussionRepository.findAll();
        for (Discussion discussion : discussions) {
            if (discussion.getMessages().remove(message)) {
                discussionRepository.save(discussion);
                break;
            }
        }

        messageRepository.delete(message);
    }
}

 */
