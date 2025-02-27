package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
