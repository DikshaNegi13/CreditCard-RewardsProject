package TransactionService.Transaction_Service.Service;

import TransactionService.Transaction_Service.DTO.*;
import TransactionService.Transaction_Service.Repository.OutboxRepository;
import TransactionService.Transaction_Service.Repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository txRepo;
    private final OutboxRepository outboxRepo;
    private final ObjectMapper mapper;

    @Transactional
    public TransactionResponse create(String userRef, CreateTransactionRequest req, String idempotencyKey){
        // Idempotency: same key returns the same transaction no matter how many times we call it
        if(idempotencyKey != null && !idempotencyKey.isBlank()){
            var existing = txRepo.findByIdempotencyKey(idempotencyKey);
            if(existing.isPresent()) return toDto(existing.get());
        }

        var tx = Transaction.builder()
                .userRef(userRef)
                .amount(req.amount())
                .currency(req.currency())
                .category(req.category())
                .merchant(req.merchant())
                .status(TransactionStatus.POSTED)
                .idempotencyKey(idempotencyKey)
                .build();
        tx = txRepo.save(tx);

        enqueueCreatedEvent(tx);
        return toDto(tx);
    }


    private void enqueueCreatedEvent(Transaction tx) {
        try {
            var evt = new TransactionCreatedEvent(
                    tx.getId(), tx.getUserRef(), tx.getAmount(), tx.getCurrency(),
                    tx.getCategory(), tx.getMerchant(), tx.getCreatedAt(), "1.0"
            );
            var payload = mapper.writeValueAsString(evt);
            var ob = OutboxEvent.builder()
                    .aggregateType("Transaction")
                    .aggregateId(tx.getId().toString())
                    .type("TransactionCreated")
                    .payload(payload)
                    .status("PENDING")
                    .build();
            outboxRepo.save(ob);
        } catch (Exception e) {
            log.error("Failed to serialize outbox event", e);
            throw new RuntimeException("serialize outbox", e);
        }
    }

    @Transactional
    public List<TransactionResponse> listMine(String userRef){
        return txRepo.findByUserRefOrderByCreatedAtDesc(userRef).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Optional<TransactionResponse> getMine(String userRef, UUID id){
        return txRepo.findById(id).filter(t -> t.getUserRef().equals(userRef)).map(this::toDto);
    }

    private TransactionResponse toDto(Transaction t){
        return new TransactionResponse(t.getId(), t.getUserRef(), t.getAmount(), t.getCurrency(),
                t.getCategory(), t.getMerchant(), t.getStatus(), t.getCreatedAt());
    }


}
