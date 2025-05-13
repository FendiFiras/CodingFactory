package tn.esprit.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Entities.Messages;
import tn.esprit.Entities.Reclamation;
import tn.esprit.Repositories.MessageRepository;
import tn.esprit.Repositories.ReclamationRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ReclamationRepository reclamationRepository;

    @PostMapping
    public Messages sendMessage(@RequestBody Messages message) {
        System.out.println("message.getReclamation(): " + message.getReclamation());
        System.out.println("message.getReclamation().getId(): " + (message.getReclamation() != null ? message.getReclamation().getIdReclamation() : "null"));

        message.setTimestamp(LocalDateTime.now());

        Long reclamationId = message.getReclamation().getIdReclamation(); // ⚠️ si id == null → erreur ici

        Reclamation existingReclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Reclamation not found with id: " + reclamationId));

        message.setReclamation(existingReclamation);
        return messageRepository.save(message);
    }


    @GetMapping("/reclamation/{id}")
    public List<Messages> getMessagesByReclamation(@PathVariable Long id) {
        return messageRepository.findByReclamation_IdReclamationOrderByTimestampAsc(id);
    }

    @GetMapping("/with-user-messages")
    public List<Long> getReclamationIdsWithUserMessages() {
        return messageRepository.findReclamationIdsWithUserMessages();
    }

}

