package co.kr.compig.api.infrastructure.auth.social.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "naverAuth", url = "https://nid.naver.com")
public interface NaverAuthApi {
	@GetMapping("/oauth2.0/token")
	ResponseEntity<String> getAccessToken(
		@RequestParam("client_id") String clientId,
		@RequestParam("client_secret") String clientSecret,
		@RequestParam("grant_type") String grantType,
		@RequestParam("redirect_url") String redirectUrl,
		@RequestParam("code") String code,
		@RequestParam("state") String state
	);
}
