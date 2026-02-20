package RewardService.rewards_service.Repository;
import java.util.UUID;

import RewardService.rewards_service.DTO.RewardLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardLedgerRepository extends JpaRepository<RewardLedgerEntry,Long> {
    boolean existsByTransactionId(UUID txId);
}
