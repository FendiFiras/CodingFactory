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
public class Reclamation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReclamation;
    private String title;
    private String description;
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private TypeReclamation typeReclamation;

    @Enumerated(EnumType.STRING)
    private TypeStatut status;

    private Integer urgencyLevel;

    @ManyToMany
    private List<Material> materials;

    @ManyToOne
    private User user;


}
