package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private String video;
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @ManyToOne
    Event event;

    @ManyToOne
    LocationEvent locationEvent;



}

