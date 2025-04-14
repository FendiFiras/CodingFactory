package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
<<<<<<<< HEAD:MicroServiceUser/src/main/java/tn/esprit/entities/Evaluation.java
========
import com.fasterxml.jackson.annotation.JsonManagedReference;
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/Evaluation.java
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
    @Lob
    @Column(length = 16777215) // For MySQL compatibility// Large Object to store binary data
    private byte[] evaluationPdf;

    @OneToOne(mappedBy = "evaluation")
    @JsonBackReference  // Child side, prevents recursion
    private Assignment assignment;




}
