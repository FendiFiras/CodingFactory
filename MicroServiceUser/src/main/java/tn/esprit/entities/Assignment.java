package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private Date endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;  // User related to the assignment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    @JsonIgnore
    private Offer offer;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluation_id_evaluation")
    @JsonManagedReference  // Parent side
    private Evaluation evaluation;
}
