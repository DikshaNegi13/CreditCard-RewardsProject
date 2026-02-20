package RewardService.rewards_service.Security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key key;
    private final String expectedIssuer;
    private final String expectedAudience;

    public JwtUtil(@Value("${security.jwt.secret}") String secret,
                   @Value("${security.jwt.issuer}") String issuer,
                   @Value("${security.jwt.audience}") String audience) {
        this.expectedIssuer = issuer;
        this.expectedAudience = audience;
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String subject(String token){
        Claims c = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//        if(expectedIssuer!=null && !expectedIssuer.equals(c.getIssuer())) throw new JwtException("bad issuer");
//        if(expectedAudience!=null && !expectedAudience.equals(c.getAudience())) throw new JwtException("bad audience");
        return c.getSubject();
    }
}
