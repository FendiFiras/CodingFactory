package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Registration {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRegistration;

    private LocalDateTime registrationDate;

    private Boolean confirmation;
    @ManyToOne
    Event event;

    @ManyToOne
    User user;



}
