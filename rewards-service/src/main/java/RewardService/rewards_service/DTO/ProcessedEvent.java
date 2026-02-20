package RewardService.rewards_service.DTO;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name="processed_events", indexes = {
        @Index(name="idx_processed_event_key", columnList="eventKey", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String eventKey; // transactionId or eventId
    @Column(nullable=false) private Instant processedAt;
}