package tn.esprit.Entities;

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
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long message_id;

    private String description;
    private String image;

    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDate;

    private Long numberOfLikes;
    private Double latitude; // Ajoutez ce champ
    private Double longitude; // Ajoutez ce champ
    private String audioUrl; // Ajouter ce champ


    @OneToMany( cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Like> likes;


    @Column(nullable = false)
    private boolean anonymous = false;


}