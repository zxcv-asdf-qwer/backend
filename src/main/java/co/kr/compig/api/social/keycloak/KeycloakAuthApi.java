package co.kr.compig.api.social.keycloak;

import co.kr.compig.api.social.dto.KeycloakRequestAccessTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "keycloakClient", url = "${auth.build-info.server-url}")
public interface KeycloakAuthApi {

  @PostMapping(value = "/realms/${auth.build-info.realm}/protocol/openid-connect/token",
      consumes = "application/x-www-form-urlencoded")
  ResponseEntity<String> getAccessToken(
      @RequestBody KeycloakRequestAccessTokenDto keycloakRequestAccessTokenDto);

}