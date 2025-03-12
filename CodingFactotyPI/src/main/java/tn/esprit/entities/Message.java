package tn.esprit.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import  lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @OneToMany( cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Like> likes;


    @Column(nullable = false)
    private boolean anonymous = false;


}