package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.MessageService;
import tn.esprit.entities.Message;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/add")
    public ResponseEntity<Message> addMessage(@RequestBody Message message,
                                              @RequestParam Long userId,
                                              @RequestParam Long discussionId) {
        Message savedMessage = messageService.addMessageToDiscussionAndUser(message, userId, discussionId);
        return ResponseEntity.ok(savedMessage);
    }


    @PutMapping("/update")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) {
        Message updatedMessage = messageService.updateMessage(message);
        return ResponseEntity.ok(updatedMessage);
    }


    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId) {
        try {
            messageService.deleteMessage(messageId);
            return ResponseEntity.ok("Message deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long message_id) {
        return ResponseEntity.ok(messageService.getMessageById(message_id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

}