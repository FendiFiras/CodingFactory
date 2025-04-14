package tn.esprit.entities;
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
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idApplication;
    @Column(nullable = true)
    private float score;
    private Date submissionDate;
    private Date Availability ;
    private String FieldofStudy;
    private String status;
    private String University;
    private String CoverLetter;
    private String CV;
    @ManyToOne
    @JoinColumn(name = "idOffer")
    private Offer offer;

}
