package tn.esprit.Services;

import tn.esprit.entities.Message;

import java.util.List;

public interface IMessageService {
    void deleteMessage(Long messageId);
    Message updateMessage(Message message);
    Message getMessageById(Long messageId);
    List<Message> getAllMessages();
    Message addMessageToDiscussionAndUser(Message message, Long userId, Long discussionId, boolean anonymous);

}