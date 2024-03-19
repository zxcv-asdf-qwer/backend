package co.kr.compig.api.infrastructure.auth.social.kakao;

import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakaoAuth", url = "https://kauth.kakao.com")
public interface KakaoAuthApi {

	@PostMapping(value = "/oauth/token")
	ResponseEntity<JSONObject> getAccessToken(
		@RequestParam("client_id") String clientId,
		@RequestParam("client_secret") String clientSecret,
		@RequestParam("grant_type") String grantType,
		@RequestParam("redirect_uri") String redirectUri,
		@RequestParam("code") String code
	);
}
