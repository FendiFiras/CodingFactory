package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;

    private String reason;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;

    @ManyToOne
    private Message message;

    @ManyToOne
    private User user;

}
