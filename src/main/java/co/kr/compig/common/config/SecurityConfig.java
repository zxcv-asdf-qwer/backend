package co.kr.compig.common.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
      httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
          SessionCreationPolicy.STATELESS);
    });
    http.cors(withDefaults());
    http.csrf(withDefaults());
    http.authorizeHttpRequests((authorize) -> authorize
        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
        .requestMatchers("/actuator/**", "/docs/**", "/test/**").permitAll()// external
        .anyRequest().authenticated()// internal
    ).oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()));
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

    return http.build();
  }

}