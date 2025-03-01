package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forum_id;

    private String title;
    private String description;
    private String image;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToMany( cascade = CascadeType.ALL)
    private List<Discussion> discussions;

    @ManyToMany
    private Set<User> Users=new HashSet<>();



}