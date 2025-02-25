package tn.esprit.entities;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
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
