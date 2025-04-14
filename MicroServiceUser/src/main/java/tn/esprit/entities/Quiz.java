package tn.esprit.entities;

<<<<<<<< HEAD:MicroServiceUser/src/main/java/tn/esprit/entities/Quiz.java
import com.fasterxml.jackson.annotation.JsonIgnore;
========
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/Quiz.java
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuiz;
    private String quizName;
    private Date deadline;
    private double minimumGrade;
    private int timeLimit;
    private int maxGrade;

<<<<<<<< HEAD:MicroServiceUser/src/main/java/tn/esprit/entities/Quiz.java

    @JsonIgnore
    @OneToOne(mappedBy = "Quiz")
    private Training training;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore

    private Set<QuizQuestion> QuizQuestions;
    @JsonIgnore

========
    @OneToOne(mappedBy = "Quiz")
    private Training training;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<QuizQuestion> QuizQuestions;
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/Quiz.java
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private Set<Response> studentResponses;


}
