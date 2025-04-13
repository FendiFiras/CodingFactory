package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvaluation;
    private float score;
    private String comment;
    @ManyToOne @JoinColumn(name = "user_id")
    private User user; // Evaluator

    @ManyToOne @JoinColumn(name = "assignment_id")
    private Assignment assignment;



}
