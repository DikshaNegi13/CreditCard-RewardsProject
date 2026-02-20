package RewardService.rewards_service.Controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.Min;

import RewardService.rewards_service.Service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Validated
@RestController
@RequestMapping("/api/rewards")
public class RewardController {


    private final RewardService service;

    public RewardController(RewardService service){
        this.service=service;
    }

    @GetMapping("/balance")
    public Map<String,Object> balance(Authentication auth){
        String userRef = (String) auth.getPrincipal();
        return Map.of("user", userRef, "points", service.currentBalance(userRef));
    }

    @PostMapping("/redeem")
    public ResponseEntity<Map<String,Object>> redeem(Authentication auth,
                                                     @RequestParam @Min(1) long points,
                                                     @RequestParam @NotBlank String reference) {
        String userRef = (String) auth.getPrincipal();
        long remaining = service.redeem(userRef, points, reference);
        return ResponseEntity.ok(Map.of("user", userRef, "remainingPoints", remaining));
    }
}
