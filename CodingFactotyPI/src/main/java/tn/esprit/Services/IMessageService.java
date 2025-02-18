package tn.esprit.Services;

import tn.esprit.entities.Message;

import java.util.List;

public interface IMessageService {
    Message addMessage(Message message, Long discussionId, Long userId);
    void deleteMessage(Long messageId);
    Message updateMessage(Message message);
    List<Message> getAllMessages();
}
