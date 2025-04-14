package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import  lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String firstName;
    private String lastName;

<<<<<<<< HEAD:MicroServiceEvent/src/main/java/tn/esprit/entities/User.java
    private LocalDate dateOfBirth;
========
    private LocalDateTime dateOfBirth;
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/User.java
    private String password;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;
    private String phoneNumber;
    private String region;



    @Column(nullable = true) // Attribut optionnel
    private String speciality;

    @Column(nullable = true) // Attribut optionnel
    private String companyName;

    @Column(nullable = true) // Attribut optionnel
    private String level;

    @Column(nullable = true) // Attribut optionne
    private String grade;
    @Column(nullable = true) // Attribut optionnel
    private String cv;
    @Column(nullable = true) // Attribut optionnel
    private Boolean accepted;
    private String address;
    private String image;
    private String otp; // Ajouter ce champ pour stocker l'OTP

    @Enumerated(EnumType.STRING)
    private Role role;


<<<<<<<< HEAD:MicroServiceEvent/src/main/java/tn/esprit/entities/User.java
========
    @ManyToMany(mappedBy = "Users")
    private Set<Training> trainings;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Quiz> Quizs;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Response> studentResponses;
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/User.java


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private UserPreference userPreference;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BanLog> banLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<LoginHistory> loginHistories;


    @OneToMany(cascade = CascadeType.ALL)
    private Set<Forum> Forums;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Discussion> discussions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToMany(cascade = CascadeType.ALL)
<<<<<<<< HEAD:MicroServiceEvent/src/main/java/tn/esprit/entities/User.java
    private List<Like> likes;
========
    private List<Like> likeees;
   
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/User.java




<<<<<<<< HEAD:MicroServiceEvent/src/main/java/tn/esprit/entities/User.java
========





    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Event> events;
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/User.java

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Offer> offers;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Application> applications;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Evaluation> evaluationss;
    @OneToOne
    private Evaluation evaluation;

    @OneToOne
    private Partnership partnership;
    @OneToOne
    private Assignment assignment;

//evenement
    @OneToMany(mappedBy = "user")
    private List<Reclamation> reclamations;

<<<<<<<< HEAD:MicroServiceEvent/src/main/java/tn/esprit/entities/User.java
    @JsonIgnore

    @OneToMany(cascade= CascadeType.ALL, mappedBy="user")
    private List<FeedBackEvent>feedBackEvents;
    @JsonIgnore
    @OneToMany(cascade= CascadeType.ALL, mappedBy="user")
    private List<Registration>registrations;

    //Traning
    @ManyToMany(mappedBy = "Users")
    private Set<Training> trainings;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set <Quiz> quizss;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Response> studentResponses;

========
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Offer> offers;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Application> applications;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Evaluation> evaluationss;
    @OneToOne
    private Evaluation evaluation;
    @OneToMany(cascade = CascadeType.ALL)
    private  Set<Registration> registrations;
    @OneToOne
    private Partnership partnership;
    @OneToOne
    private Assignment assignment;
>>>>>>>> pfe-managment:MicroservicePfe/src/main/java/tn/esprit/entities/User.java
}

