package RewardService.rewards_service.DTO;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="redemptions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Redemption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String userRef;
    @Column(nullable=false) private long pointsRedeemed;
    @Column(nullable=false) private String reference; // e.g. "VOUCHER-123" or orderId
    @Column(nullable=false) private Instant createdAt;

    @PrePersist void onCreate(){ if(createdAt==null) createdAt = Instant.now(); }
}