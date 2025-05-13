package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender; // "admin" ou "user"
    private String content;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "reclamation_id")
    private Reclamation reclamation; // Lier les messages à une réclamation spécifique
}

