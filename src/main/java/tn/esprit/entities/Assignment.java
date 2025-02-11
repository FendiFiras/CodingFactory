package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAffectation;
    private String status;
    private Date startDate;
    private Date endDatee;
    @ManyToOne @JoinColumn(name = "user_id")
    private User user; // Assigned by User (Manager)

    @ManyToOne @JoinColumn(name = "partnership_id")
    private Partnership partnership;

    @OneToMany(mappedBy = "assignment")
    private List<Evaluation> evaluations;

}
