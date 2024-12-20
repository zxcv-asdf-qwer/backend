package co.kr.compig.api.infrastructure.auth.keycloak;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.kr.compig.api.infrastructure.auth.keycloak.model.KeycloakAccessTokenRequest;
import jakarta.ws.rs.core.MediaType;

@FeignClient(name = "keycloakClient", url = "${auth.build-info.server-url}")
public interface KeycloakAuthApi {

	@PostMapping(value = "/realms/${auth.build-info.realm}/protocol/openid-connect/token",
		consumes = MediaType.APPLICATION_FORM_URLENCODED)
	ResponseEntity<String> getAccessToken(
		@RequestBody KeycloakAccessTokenRequest keycloakAccessTokenRequest);

}
