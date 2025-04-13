package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String password;
    private LocalDateTime dateOfBirth;

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


    @ManyToMany(mappedBy="Users",cascade = CascadeType.ALL)
    private Set<Training>trainings;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Quiz> Quizs;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<StudentResponse> studentResponses;

    @OneToOne
    private UserPreference userPreference;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserBanLog> banLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<LoginHistory> loginHistories;




    @OneToMany(cascade = CascadeType.ALL)
    private Set<Forum> Forums;

    @OneToMany( cascade = CascadeType.ALL)
    private List<Discussion> discussions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes;
    @OneToMany(mappedBy = "user")
    private List<Partnership> partnerships;

    @OneToMany(mappedBy = "user")
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "user")
    private List<Evaluation> evaluations;

    @OneToMany(mappedBy = "user")
    private List<Offer> offers;

    @OneToMany(mappedBy = "user")
    private List<Application> applications;


    @OneToMany(mappedBy = "user")
    private List<Reclamation> reclamations;

    @JsonIgnore

    @OneToMany(cascade= CascadeType.ALL, mappedBy="user")
    private List<FeedBackEvent>feedBackEvents;
    @JsonIgnore
    @OneToMany(cascade= CascadeType.ALL, mappedBy="user")
    private List<Registration>registrations;



}
