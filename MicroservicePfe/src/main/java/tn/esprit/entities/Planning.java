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
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlanning;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private String description;
    @ManyToOne
    Event event;
    @ManyToOne
    LocationEvent locationEvent;

}

