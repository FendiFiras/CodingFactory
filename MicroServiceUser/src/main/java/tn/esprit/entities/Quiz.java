package tn.esprit.entities;
import jakarta.persistence.*;
import  lombok.*;

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

    @OneToOne (mappedBy="Quiz")
    private Training training;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<QuizQuestion> QuizQuestions;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="quiz")
    private Set<StudentResponse> studentResponses;



}
