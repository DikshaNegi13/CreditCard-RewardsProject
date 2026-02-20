package TransactionService.Transaction_Service.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtil {
    private final Key key;
    private final String expectedIssuer;
    private final String expectedAudience;

    public JwtUtil(@Value("${app.security.jwt.secret}") String secret,
                   @Value("${app.security.jwt.issuer}") String issuer,
                   @Value("${app.security.jwt.audience}") String audience) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expectedIssuer = issuer;
        this.expectedAudience = audience;
    }

    public String extractSubject(String token){
        Claims c = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//        if(expectedIssuer!=null && !expectedIssuer.equals(c.getIssuer())) throw new JwtException("bad issuer");
//        if(expectedAudience!=null && !expectedAudience.equals(c.getAudience())) throw new JwtException("bad audience");
        return c.getSubject();
    }
}
