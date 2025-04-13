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
    private float score;
    private Date submissionDate;
    private String status;




}
