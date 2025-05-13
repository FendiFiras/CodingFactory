package tn.esprit.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBan;

    private Timestamp banDuration;
    private String banReason;


    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private User user;


}
