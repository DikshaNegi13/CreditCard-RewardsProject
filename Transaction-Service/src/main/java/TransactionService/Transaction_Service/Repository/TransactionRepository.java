package TransactionService.Transaction_Service.Repository;

import TransactionService.Transaction_Service.DTO.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserRefOrderByCreatedAtDesc(String userRef);
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

}
