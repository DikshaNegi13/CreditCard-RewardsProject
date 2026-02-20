package TransactionService.Transaction_Service.Service;

import TransactionService.Transaction_Service.DTO.OutboxEvent;
import TransactionService.Transaction_Service.Repository.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {
    private final OutboxRepository outboxRepo;
    private final KafkaTemplate<String, String> kafka;
    @Value("${tx.kafka.topic.created}") private String createdTopic;
    @Value("${tx.outbox.batch-size:50}") private int batchSize;
    @Value("${tx.outbox.max-retries:5}") private int maxRetries;
    @Scheduled(fixedDelayString = "${tx.outbox.publish-interval-ms:1000}")
    @Transactional
    public void publish() {
        List<OutboxEvent> batch = outboxRepo.pickBatch(maxRetries, PageRequest.of(0, batchSize));
        for(OutboxEvent e : batch){
            try {
                String topic = switch (e.getType()) {
                    case "TransactionCreated" -> createdTopic;
                    default -> createdTopic; // extendable
                };
                // use aggregateId (transactionId) as key for ordering
                kafka.send(new ProducerRecord<>(topic, e.getAggregateId(), e.getPayload()));
                e.setStatus("SENT");
                e.setAttempts(e.getAttempts()+1);
            } catch (Exception ex){
                e.setStatus("FAILED");
                e.setAttempts(e.getAttempts()+1);
                e.setLastError(ex.getMessage());
                log.warn("Outbox publish failed id={} attempts={} err={}", e.getId(), e.getAttempts(), ex.getMessage());
            }
        }
        // JPA dirty tracking will persist updates on tx commit
    }

}
