package co.kr.compig.common.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final KeycloakLogoutHandler keycloakLogoutHandler;

  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.csrf(withDefaults());
//    http.cors(withDefaults());
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers(new AntPathRequestMatcher("/pb/**"))
        .hasRole("USER")
        .requestMatchers(new AntPathRequestMatcher("/pv/**"))
        .hasRole("ADMIN")
        .requestMatchers(
            new AntPathRequestMatcher("/actuator/**"),
            new AntPathRequestMatcher("/docs/**"),
            antMatcher(HttpMethod.GET, "/social/**"),
            antMatcher(HttpMethod.GET, "/members/**"),
            antMatcher(HttpMethod.POST, "/members/**")
            )
        .permitAll()
        .anyRequest()
        .authenticated());
    http.exceptionHandling(exceptionHandling -> exceptionHandling
        .authenticationEntryPoint(customAuthenticationEntryPoint())
        .accessDeniedHandler(customAccessDeniedHandler()));
    http.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt ->
        jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter())
    ));
    http.oauth2Login(withDefaults())
        .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    return http.build();
  }

  private AuthenticationEntryPoint customAuthenticationEntryPoint() {
    return (request, response, accessDeniedException) -> {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().print("{" + "\"message\"" + ":" + "\"로그인을 먼저 진행해주세요\"" + "}");
    };
  }

  private AccessDeniedHandler customAccessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().print("{" + "\"message\"" + ":" + "\"접근 권한이 없습니다\"" + "}");
    };
  }

  private Converter<Jwt, AbstractAuthenticationToken> customJwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        new CustomJwtGrantedAuthoritiesConverter());
    return jwtAuthenticationConverter;
  }

  private final class CustomJwtGrantedAuthoritiesConverter implements
      Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
      var realmAccess = (Map<String, List<String>>) jwt.getClaim("realm_access");
      if (realmAccess == null || realmAccess.isEmpty()) {
        return List.of(); // Return an empty list if the realm_access claim is not available
      }
      return realmAccess.get("roles").stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
          .collect(Collectors.toList());
    }
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}