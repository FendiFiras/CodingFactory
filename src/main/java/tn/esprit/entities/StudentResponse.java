package tn.esprit.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResponse;
    private String givenResponse;
    private boolean isCorrect;
    private boolean scoreObtained;
    @ManyToOne
    Quiz quiz;

}
