package tn.esprit.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.Message;
import tn.esprit.entities.User;
import tn.esprit.entities.Discussion;

import java.util.Date;
import java.util.Optional;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;
    private static final String UPLOAD_DIR = "C:/uploads/";



    public Message addMessageToDiscussionAndUser(Message message, Long userId, Long discussionId) {

        System.out.println("Enregistrement du message avec latitude : " + message.getLatitude());
        System.out.println("Enregistrement du message avec longitude : " + message.getLongitude());
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Discussion> discussionOpt = discussionRepository.findById(discussionId);

        if (userOpt.isPresent() && discussionOpt.isPresent()) {
            User user = userOpt.get();
            Discussion discussion = discussionOpt.get();

            // Assigner la date de création du message

            message.setMessageDate(new Date());

            // Sauvegarder le message
            message = messageRepository.save(message);

            // Ajouter le message à la discussion
            discussion.getMessages().add(message);
            discussionRepository.save(discussion);

            return message;
        } else {
            throw new IllegalArgumentException("User or Discussion not found");
        }
    }

    public List<Message> getAllMessagesForDiscussion(Long discussionId) {
        Optional<Discussion> discussionOpt = discussionRepository.findById(discussionId);
        if (discussionOpt.isPresent()) {
            return discussionOpt.get().getMessages();
        } else {
            throw new IllegalArgumentException("Discussion not found with ID: " + discussionId);
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

    public String saveImage(MultipartFile image) throws IOException {
        // Créez le dossier de téléchargement s'il n'existe pas
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générez un nom de fichier unique
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Sauvegardez l'image sur le disque
        Files.copy(image.getInputStream(), filePath);

        // Retournez l'URL de l'image
        return fileName; // Retournez uniquement le nom du fichier
    }
}