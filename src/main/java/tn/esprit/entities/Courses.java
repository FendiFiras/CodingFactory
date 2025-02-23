package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import  lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Courses {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String courseName;
    private String courseDescription;
    // 🔥 On stocke plusieurs fichiers sous forme d'une seule chaîne de texte, séparée par des virgules
    private String fileUrls;

    @Enumerated(EnumType.STRING)
    private CourseDifficulty difficulty;
    @JsonIgnore

    @ManyToOne
    Training training;
    @JsonIgnore
    @OneToMany(cascade= CascadeType.ALL, mappedBy="courses")
    private Set<Session> sessions;

}
