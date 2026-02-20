package TransactionService.Transaction_Service.DTO;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="outbox_events", indexes={
        @Index(name="idx_outbox_status_created", columnList="status,createdAt")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable=false) private String aggregateType; // "Transaction"
    @Column(nullable=false) private String aggregateId;   // transaction UUID as string
    @Column(nullable=false) private String type;          // "TransactionCreated"
    @Lob @Column(nullable=false) private String payload;  // JSON
    @Column(nullable=false) private String status;        // PENDING, SENT, FAILED
    @Column(nullable=false) private Integer attempts;
    @Column(nullable=false) private Instant createdAt;
    private String lastError;
    public String getType() {
        return this.type;
    }

    @PrePersist void onCreate(){
        if(createdAt==null) createdAt = Instant.now();
        if(attempts==null) attempts = 0;
        if(status==null) status = "PENDING";
    }
}
