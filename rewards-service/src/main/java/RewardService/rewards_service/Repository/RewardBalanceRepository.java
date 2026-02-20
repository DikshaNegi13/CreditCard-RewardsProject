package RewardService.rewards_service.Repository;

import RewardService.rewards_service.DTO.RewardBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RewardBalanceRepository extends JpaRepository<RewardBalance,Long> {
Optional<RewardBalance> findByUserRef(String userRef);
}
