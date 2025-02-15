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
    @Column(nullable = false)
    private LocalDateTime loginTime;
    @Column(nullable = false)
    private LocalDateTime logoutTime;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false)
    private String ipAddress;
    @Column(nullable = false)
    private String macAddress;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String deviceInfo;
    @ManyToOne
    private User user;

}
