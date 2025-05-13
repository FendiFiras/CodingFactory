package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

public class Training {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainingId;
    private String trainingName;
    private Date startDate;
    private Date endDate;
    private double price;

    @Enumerated(EnumType.STRING)
    private TrainingType type;
    @JsonIgnore

    @ManyToMany
    private Set<User> Users=new HashSet<>();
    @JsonIgnore

    @OneToMany(cascade= CascadeType.ALL, mappedBy="training")
    private Set<Courses> coursess;
    @JsonIgnore  // ✅ Ajoutez cela sur les relations
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)  // ✅ Supprime aussi le quiz si nécessaire
    private Quiz Quiz;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Payment> payments;




}
