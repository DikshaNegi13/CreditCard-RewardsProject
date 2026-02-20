package RewardService.rewards_service.DTO;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="reward_balances", indexes = {
        @Index(name="idx_balance_user", columnList="userRef", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String userRef;
    @Column(nullable=false) private long points; // store as whole-number points
    @Version private Long version; // optimistic locking
}