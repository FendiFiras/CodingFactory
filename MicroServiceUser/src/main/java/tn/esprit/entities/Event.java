package tn.esprit.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private Set<Planning> Plannings;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="events")
    private Set<Notification>notifications;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="Revent")
    private Set<Registration>Registrations;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="FeedEvent")
    private Set<FeedBackEvent>feedBackEvents;

    @OneToMany(cascade= CascadeType.ALL)
    private Set<Payment> payments;

    @ManyToOne
    User user;

}