package tn.esprit.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedBackEvent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFeedback;

    private int rating;

    private String comments;

    private LocalDateTime feedbackDate;

    @ManyToOne
    Event event;

    @ManyToOne
    User user;



}
