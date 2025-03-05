    package tn.esprit.Controllers;

    import lombok.RequiredArgsConstructor;
    import java.util.List;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import tn.esprit.Services.MessageService;
    import tn.esprit.entities.Message;
    import org.springframework.web.multipart.MultipartFile;

    @CrossOrigin(origins = "http://localhost:4200")
    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/messages")
    public class MessageController {

        private final MessageService messageService;

        @PostMapping("/add")
        public ResponseEntity<Message> addMessage(
                @RequestParam("userId") Long userId,
                @RequestParam("discussionId") Long discussionId,
                @RequestParam("description") String description,
                @RequestParam(value = "anonymous", required = false, defaultValue = "false") boolean anonymous) {

            try {
                Message message = new Message();
                message.setDescription(description);
                message.setAnonymous(anonymous); // Définit le mode anonyme

                Message savedMessage = messageService.addMessageToDiscussionAndUser(message, userId, discussionId);
                return ResponseEntity.ok(savedMessage);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @PostMapping(value = "/add-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Message> addMessageWithImage(
                @RequestParam("userId") Long userId,
                @RequestParam("discussionId") Long discussionId,
                @RequestParam("description") String description,
                @RequestParam(value = "image", required = false) MultipartFile image,
                @RequestParam(value = "anonymous", required = false, defaultValue = "false") boolean anonymous) {

            try {
                Message message = new Message();
                message.setDescription(description);
                message.setAnonymous(anonymous); // Ajout du mode anonyme

                // Ajoutez l'image au message si elle est fournie
                if (image != null && !image.isEmpty()) {
                    String imageUrl = messageService.saveImage(image); // Méthode pour sauvegarder l'image
                    message.setImage(imageUrl); // Utilisez le champ `image` de votre entité
                }

                Message savedMessage = messageService.addMessageToDiscussionAndUser(message, userId, discussionId);
                return ResponseEntity.ok(savedMessage);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @GetMapping("/discussion/{discussionId}")
        public ResponseEntity<List<Message>> getMessagesForDiscussion(@PathVariable Long discussionId) {
            try {
                List<Message> messages = messageService.getAllMessagesForDiscussion(discussionId);
                return ResponseEntity.ok(messages);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }



        @PutMapping(value = "/{messageId}/update-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Message> updateMessageWithImage(
                @PathVariable Long messageId,
                @RequestParam("description") String description,
                @RequestParam(value = "image", required = false) MultipartFile image) {

            try {
                Message message = messageService.getMessageById(messageId);
                message.setDescription(description);

                // Ajoutez la nouvelle image au message si elle est fournie
                if (image != null && !image.isEmpty()) {
                    String imageUrl = messageService.saveImage(image); // Méthode pour sauvegarder l'image
                    message.setImage(imageUrl); // Utilisez le champ `image` de votre entité
                }

                Message updatedMessage = messageService.updateMessage(message);
                return ResponseEntity.ok(updatedMessage);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
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

        @PutMapping("/update/{messageId}")
        public ResponseEntity<Message> updateMessage(@PathVariable Long messageId, @RequestBody Message message) {
            message.setMessage_id(messageId); // Ensure the message ID is set
            Message updatedMessage = messageService.updateMessage(message);
            return ResponseEntity.ok(updatedMessage);
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