package RewardService.rewards_service.Service;

import RewardService.rewards_service.DTO.Category;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class PointsPolicy {

    private final Map<String,Integer> multipliers;
    public PointsPolicy(Map<String,Integer> multipliers) {
        Map<String,Integer>value=new HashMap<>();
        List<String> lt=new ArrayList<>(Arrays.asList("GROCERIES","DINING","TRAVEL","FUEL","UTILITIES","SHOPPING"));
        for(int i=0;i<6;i++){
            value.put(lt.get(i),i+1);
        }

        this.multipliers = value;
    }

    // Example rule: points = floor(amount / 100) * multiplier
    public long computePoints(BigDecimal amount, Category category) {
        long base = amount.movePointLeft(2).setScale(0, java.math.RoundingMode.FLOOR).longValue(); // per ₹100
        int mult = multipliers.getOrDefault(category.name(), multipliers.getOrDefault("DEFAULT", 1));
        return base * mult;
    }
}