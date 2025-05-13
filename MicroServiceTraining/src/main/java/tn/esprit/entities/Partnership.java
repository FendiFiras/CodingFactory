package tn.esprit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partnership {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idPartnership;
    private String CompanyName;
    private String Industry;
    private String CompanyWebsite;
    private String CompanyLogo;

}
