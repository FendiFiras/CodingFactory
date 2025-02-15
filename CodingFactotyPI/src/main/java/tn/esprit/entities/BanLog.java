package tn.esprit.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.*;
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
