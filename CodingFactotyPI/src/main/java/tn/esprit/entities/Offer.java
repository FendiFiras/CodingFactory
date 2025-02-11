package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffer;
    private String title;
    private String description;
    private String requiredSkill;
    private String duration;
    private String status;
    @ManyToOne @JoinColumn(name = "user_id")
    private User user; // Created by a Company User

    @OneToMany(mappedBy = "offer")
    private List<Application> applications;


}
