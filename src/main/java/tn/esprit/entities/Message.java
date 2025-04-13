package tn.esprit.entities;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import  lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    @ManyToOne
    private Discussion discussion;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

}
