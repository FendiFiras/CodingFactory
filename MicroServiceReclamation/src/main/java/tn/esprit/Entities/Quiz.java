package tn.esprit.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @JsonIgnore
    @OneToOne(mappedBy = "Quiz")
    private Training training;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore

    private Set<QuizQuestion> QuizQuestions;
    @JsonIgnore

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private Set<Response> studentResponses;


}
