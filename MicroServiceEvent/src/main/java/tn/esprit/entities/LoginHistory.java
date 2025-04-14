package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLoginHistory;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private Boolean active;
    private String ipAddress;
    private String macAddress;
    private String location;
    private String deviceInfo;
    @ManyToOne
    private User user;

}
