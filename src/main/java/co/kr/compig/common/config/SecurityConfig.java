package co.kr.compig.common.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private static final String GROUPS = "groups";
  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private final KeycloakLogoutHandler keycloakLogoutHandler;
  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers(new AntPathRequestMatcher("/actuator/**"), new AntPathRequestMatcher("/docs/**"))
        .permitAll()
        .anyRequest()
        .authenticated());
    http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
    http.oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    return http.build();
  }
  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
    return authorities -> {
      Set mappedAuthorities = new HashSet<>();
      var authority = authorities.iterator().next();
      boolean isOidc = authority instanceof OidcUserAuthority;

      if (isOidc) {
        var oidcUserAuthority = (OidcUserAuthority) authority;
        var userInfo = oidcUserAuthority.getUserInfo();

        // Tokens can be configured to return roles under
        // Groups or REALM ACCESS hence have to check both
        if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
          var realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
          var roles = (Collection) realmAccess.get(ROLES_CLAIM);
          mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
        } else if (userInfo.hasClaim(GROUPS)) {
          Collection roles = (Collection) userInfo.getClaim(
              GROUPS);
          mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
        }
      } else {
        var oauth2UserAuthority = (OAuth2UserAuthority) authority;
        Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

        if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
          Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(
              REALM_ACCESS_CLAIM);
          Collection roles = (Collection) realmAccess.get(ROLES_CLAIM);
          mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
        }
      }
      return mappedAuthorities;
    };
  }

  Collection generateAuthoritiesFromClaim(Collection roles) {
    return (Collection) roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
        Collectors.toList());
  }
}