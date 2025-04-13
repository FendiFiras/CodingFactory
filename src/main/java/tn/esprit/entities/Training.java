package tn.esprit.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.Date;
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
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> Users;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="training")
    private Set<Courses> coursess;
    @OneToOne
    private Quiz Quiz;



}
