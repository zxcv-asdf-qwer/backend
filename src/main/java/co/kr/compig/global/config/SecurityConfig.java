package co.kr.compig.global.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import co.kr.compig.global.security.converter.CustomJwtAuthenticationConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

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
		// http.csrf(withDefaults());
		// http.cors(withDefaults());
		http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests(auth -> auth
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/pb/**"))
			.hasRole("USER")
			.requestMatchers(new AntPathRequestMatcher("/pv/**"))
			.hasRole("ADMIN")
			.requestMatchers(
				new AntPathRequestMatcher("/actuator/**"),
				new AntPathRequestMatcher("/docs/**"),
				antMatcher("/social/**"),
				antMatcher(HttpMethod.GET, "/members/**"),
				antMatcher(HttpMethod.POST, "/members/**"),
				antMatcher(HttpMethod.GET, "/sms/**"),
				antMatcher(HttpMethod.POST, "/sms/**")
			)
			.permitAll()
			.anyRequest()
			.authenticated());
		http.exceptionHandling(exceptionHandling -> exceptionHandling
			.authenticationEntryPoint(customAuthenticationEntryPoint())
			.accessDeniedHandler(customAccessDeniedHandler()));
		http.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt ->
			jwt.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter("compig-back"))));
		http.logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));
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

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
