package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("banLogs") // Ignore la liste des BanLogs dans l'utilisateur pour éviter la boucle infinie
    private User user;


}
