package tn.esprit.entities;
import jakarta.persistence.*;
import  lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayment;

    private Double amount;

    private LocalDateTime paymentDate;



    private String transactionId;

    private String receiptUrl;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Training> trainingS;


}
