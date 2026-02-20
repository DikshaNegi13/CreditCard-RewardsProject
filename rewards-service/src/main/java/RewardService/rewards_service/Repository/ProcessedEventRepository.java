package RewardService.rewards_service.Repository;

import RewardService.rewards_service.DTO.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent,Long> {
 boolean existsByEventKey(String eventKey);
}
