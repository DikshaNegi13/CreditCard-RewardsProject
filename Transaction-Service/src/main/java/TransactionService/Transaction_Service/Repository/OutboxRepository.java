package TransactionService.Transaction_Service.Repository;

import TransactionService.Transaction_Service.DTO.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID>{
    @Query("""
    SELECT e FROM OutboxEvent e 
    WHERE e.status='PENDING' 
       OR (e.status='FAILED' AND e.attempts < :maxRetries)
    ORDER BY e.createdAt ASC
  """)
    List<OutboxEvent> pickBatch(@Param("maxRetries") int maxRetries, org.springframework.data.domain.Pageable pageable);

    List<OutboxEvent> findByStatusAndCreatedAtBefore(String status, Instant before);

}
