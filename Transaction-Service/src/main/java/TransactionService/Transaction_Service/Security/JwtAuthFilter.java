package TransactionService.Transaction_Service.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwt;
    public JwtAuthFilter(JwtUtil jwt){ this.jwt = jwt; }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("auth :"+auth);
        if(auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                String subject = jwt.extractSubject(token); // username/email from user-service
                System.out.println("subject :"+subject);
                var authToken = new UsernamePasswordAuthenticationToken(subject, null, List.of());
                System.out.println("authtoken :"+authToken);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch(Exception e) {
                System.out.println("exception occures"+ e.getMessage());

            }
        }
        chain.doFilter(request, response);
    }
}
