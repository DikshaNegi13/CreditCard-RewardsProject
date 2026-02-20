package RewardService.rewards_service.DTO;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="reward_ledger", indexes = {
        @Index(name="idx_ledger_user", columnList="userRef"),
        @Index(name="idx_ledger_tx", columnList="transactionId", unique = true)
})
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RewardLedgerEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(nullable=false) private String userRef;
    @Column(nullable=false) private UUID transactionId;
    @Column(nullable=false, precision=18, scale=2) private BigDecimal amount;
    @Column(nullable=false, length=3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Category category;

    @Column(nullable=false) private long pointsEarned;
    @Column(nullable=false) private Instant createdAt;

    @PrePersist void onCreate(){ if(createdAt==null) createdAt = Instant.now(); }
}