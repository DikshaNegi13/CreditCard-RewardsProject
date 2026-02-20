package TransactionService.Transaction_Service.DTO;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.time.Instant;


@Entity@Table(name="transactions",indexes={
        @Index(name="idx_tx_userref", columnList = "userRef"),
        @Index(name="idx_tx_idem", columnList = "idempotencyKey", unique = true)
})
@Getter
@Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false) private String userRef;
    @Column(nullable=false, precision=18, scale=2) private BigDecimal amount;
    @Column(nullable=false, length=3) private String currency; // ISO-4217
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Category category;
    @Column(nullable=false) private String merchant;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TransactionStatus status;
    @Column(nullable=false, updatable=false) private Instant createdAt;
    @Column(unique = true) private String idempotencyKey;

    @PrePersist void onCreate(){ if(createdAt==null) createdAt = Instant.now(); }


}
