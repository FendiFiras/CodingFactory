package tn.esprit.entities;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import  lombok.*;

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





    @OneToMany( cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Like> likeees;

}