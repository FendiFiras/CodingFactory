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

