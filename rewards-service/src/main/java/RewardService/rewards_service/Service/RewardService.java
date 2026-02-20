package RewardService.rewards_service.Service;
import RewardService.rewards_service.DTO.*;
import RewardService.rewards_service.Repository.*;
import RewardService.rewards_service.Repository.RewardBalanceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import RewardService.rewards_service.DTO.RewardBalance;
import RewardService.rewards_service.DTO.ProcessedEvent;
import RewardService.rewards_service.DTO.RewardLedgerEntry;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.time.Instant;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service @Slf4j
public class RewardService {

    private final ProcessedEventRepository processedEventRepo;
    private final RewardBalanceRepository balanceRepo;
    private final RewardLedgerRepository ledgerRepo;
    private final RedemptionRepository redemptionRepo;
    private final PointsPolicy policy;

    public RewardService(ProcessedEventRepository processedEventRepo,RewardBalanceRepository balanceRepo,RewardLedgerRepository ledgerRepo,RedemptionRepository redemptionRepo,PointsPolicy policy){
        this.balanceRepo=balanceRepo;
        this.ledgerRepo=ledgerRepo;
        this.policy=policy;
        this.redemptionRepo=redemptionRepo;
        this.processedEventRepo=processedEventRepo;
    }
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @KafkaListener(topics = "${rewards.topic}", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onTransactionCreated(@Payload String json) throws Exception {
        var evt = mapper.readValue(json, TransactionCreatedEvent.class);
        String eventKey = "tx:" + evt.transactionId().toString();

        // idempotency: ensure each transaction affects points once
        if (processedEventRepo.existsByEventKey(eventKey)) {
//            log.debug("duplicate event ignored {}", eventKey);
            return;
        }

        long points = policy.computePoints(evt.amount(), evt.category());

        // 1) append immutable ledger entry
        var entry = RewardLedgerEntry.builder()
                .userRef(evt.userRef())
                .transactionId(evt.transactionId())
                .amount(evt.amount())
                .currency(evt.currency())
                .category(evt.category())
                .pointsEarned(points)
                .build();
        ledgerRepo.save(entry);

        // 2) upsert balance with optimistic locking
        var balance = balanceRepo.findByUserRef(evt.userRef())
                .orElseGet(() -> RewardBalance.builder().userRef(evt.userRef()).points(0).build());
        balance.setPoints(balance.getPoints() + points);
        balanceRepo.save(balance);

        // 3) mark processed
        processedEventRepo.save(ProcessedEvent.builder()
                .eventKey(eventKey)
                .processedAt(Instant.now())
                .build());

        log.info("awarded points user={} tx={} points={}", evt.userRef(), evt.transactionId(), points);
    }

    @Transactional(readOnly = true)
    public long currentBalance(String userRef){
        return balanceRepo.findByUserRef(userRef).map(RewardBalance::getPoints).orElse(0L);
    }

    @Transactional
    public long redeem(String userRef, long points, String reference){
        var balance = balanceRepo.findByUserRef(userRef)
                .orElseThrow(() -> new IllegalArgumentException("No balance for user"));
        if(points <=0) throw new IllegalArgumentException("Invalid points");
        if(balance.getPoints() < points) throw new IllegalStateException("Insufficient points");

        balance.setPoints(balance.getPoints() - points);
        balanceRepo.save(balance);

        redemptionRepo.save(Redemption.builder()
                .userRef(userRef)
                .pointsRedeemed(points)
                .reference(reference)
                .build());

        return balance.getPoints();
    }
}
