package TransactionService.Transaction_Service.Service;

import TransactionService.Transaction_Service.DTO.CreateTransactionRequest;
import TransactionService.Transaction_Service.DTO.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            Authentication auth,
            @Valid @RequestBody CreateTransactionRequest req,
            @RequestHeader(name = "Idempotency-Key", required = false) String idemKey
    ){
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userRef = (String) auth.getPrincipal(); // get username from JWT
        return ResponseEntity.ok(service.create(userRef, req, idemKey));
    }

    @GetMapping
    public List<TransactionResponse> myTransactions(Authentication auth){
        return service.listMine((String)auth.getPrincipal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> byId(Authentication auth, @PathVariable UUID id){
        return service.getMine((String)auth.getPrincipal(), id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
