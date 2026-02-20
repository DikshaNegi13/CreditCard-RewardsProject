package RewardService.rewards_service.Repository;

import RewardService.rewards_service.DTO.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedemptionRepository extends JpaRepository<Redemption,Long> {

}
