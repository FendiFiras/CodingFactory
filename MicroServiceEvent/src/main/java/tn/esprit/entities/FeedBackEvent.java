package tn.esprit.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)

public class FeedBackEvent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFeedback;

    private int rating;

    private String comments;

    private LocalDateTime feedbackDate;

    @ManyToOne
    Event event;
@JsonIgnore
    @ManyToOne
    User user;



}
