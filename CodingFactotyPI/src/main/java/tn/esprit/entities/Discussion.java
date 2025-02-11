package tn.esprit.entities;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discussion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discussion_id;

    private String description;
    private Long numberOfLikes;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;

    @ManyToOne
    private Forum forum;



    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;


}
