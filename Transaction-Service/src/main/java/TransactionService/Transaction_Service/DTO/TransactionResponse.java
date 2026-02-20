package TransactionService.Transaction_Service.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse (
        UUID id, String userRef, BigDecimal amount, String currency,
        Category category, String merchant, TransactionStatus status, Instant createdAt
) {}
