/*
package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.IMessageService;
import tn.esprit.entities.Message;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    @PostMapping("/add/{idUser}/{discussionId}")
    public ResponseEntity<Message> addMessage(@RequestBody Message message,
                                              @PathVariable Long idUser,
                                              @PathVariable Long discussionId) {
        Message newMessage = messageService.addMessage(message, idUser, discussionId);
        return ResponseEntity.ok(newMessage);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long messageId) {
        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @PutMapping("/update/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long messageId,
                                                 @RequestBody Message message) {
        return ResponseEntity.ok(messageService.updateMessage(messageId, message));
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}

 */
