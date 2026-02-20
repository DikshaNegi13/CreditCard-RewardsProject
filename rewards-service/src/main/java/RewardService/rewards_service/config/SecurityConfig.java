package RewardService.rewards_service.config;

import RewardService.rewards_service.Security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain chain(HttpSecurity http, JwtAuthFilter jwt) throws Exception {
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(req->req
                        .requestMatchers("/actuator/**","/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**").permitAll()
                        .requestMatchers("/api/rewards/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwt, BasicAuthenticationFilter.class);
        return http.build();
    }
}