package tn.esprit.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    private String title;
    @Column(length = 50000)
    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String location;

    private int maxParticipants;

    private LocalDateTime registrationDeadline;

    private double price;

    private String imageUrl;

    private String videoUrl;
    @Enumerated(EnumType.STRING)
    private EventType eventType;


    @OneToMany(cascade= CascadeType.ALL, mappedBy="event")
    private List<Planning> Plannings;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="event")
    private List<Notification>notifications;
      @JsonIgnore
    @OneToMany(cascade= CascadeType.ALL, mappedBy="event")
    private List<Registration>Registrations;

      @JsonIgnore
    @OneToMany(cascade= CascadeType.ALL, mappedBy="event")
    private List<FeedBackEvent>feedBackEvents;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Payment> payemnts;

}
