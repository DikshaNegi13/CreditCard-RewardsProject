package RewardService.rewards_service.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionCreatedEvent(
        UUID transactionId,
        String userRef,
        BigDecimal amount,
        String currency,
        Category category,
        String merchant,
        Instant createdAt,
        String schemaVersion
) {}
