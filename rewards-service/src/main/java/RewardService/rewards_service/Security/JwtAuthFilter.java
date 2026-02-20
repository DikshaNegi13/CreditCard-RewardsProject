package RewardService.rewards_service.Security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter implements Filter {
    private final JwtUtil jwt;
    public JwtAuthFilter(JwtUtil jwt){ this.jwt = jwt; }

    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(auth!=null && auth.startsWith("Bearer ")){
            try {
                String sub = jwt.subject(auth.substring(7));
                var token = new UsernamePasswordAuthenticationToken(sub, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(token);
            } catch (Exception e) {
                System.out.println("exception occured"+e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }


}

