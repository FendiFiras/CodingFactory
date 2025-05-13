package tn.esprit.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import  lombok.*;

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

    private LocalDateTime dateOfBirth;
    private String password;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;
    private String phoneNumber;

    @Column(nullable = true) // Attribut optionnel
    private String cv;

    @Column(nullable = true) // Attribut optionnel
    private String speciality;

    @Column(nullable = true) // Attribut optionnel
    private String companyName;

    @Column(nullable = true) // Attribut optionnel
    private String level;

    @Column(nullable = true) // Attribut optionne
    private String grade;

    private String address;
    private String image;

    @Enumerated(EnumType.STRING)
    private Role role;


    @ManyToMany(mappedBy = "Users")
    private Set<Training> trainings;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set <Quiz> quizss;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Response> studentResponses;

    @OneToOne
    private UserPreference userPreference;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserBanLog> banLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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
    private List<Like> likeees;
   









    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Event> events;

    @OneToMany(mappedBy = "user")
    private List<Reclamation> reclamations;

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
}

